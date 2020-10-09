package br.com.f5promotora.crm.domain.service.v1.account;

import br.com.f5promotora.crm.domain.data.entity.jpa.account.Company;
import br.com.f5promotora.crm.domain.data.entity.jpa.account.Profile;
import br.com.f5promotora.crm.domain.data.enums.ProfilePermission;
import br.com.f5promotora.crm.domain.data.enums.ProfileRole;
import br.com.f5promotora.crm.domain.data.enums.ProfileStatus;
import br.com.f5promotora.crm.domain.data.v1.dto.ImportResult;
import br.com.f5promotora.crm.domain.data.v1.dto.ProfileDTO;
import br.com.f5promotora.crm.domain.data.v1.filter.CompanyFilter;
import br.com.f5promotora.crm.domain.data.v1.filter.ProfileFilter;
import br.com.f5promotora.crm.domain.data.v1.form.AuthPassword;
import br.com.f5promotora.crm.domain.data.v1.form.CompanyFormCreate;
import br.com.f5promotora.crm.domain.data.v1.form.ProfileFormCreate;
import br.com.f5promotora.crm.domain.data.v1.mapper.ProfileMapper;
import br.com.f5promotora.crm.domain.service.CompanyService;
import br.com.f5promotora.crm.domain.service.ProfileService;
import br.com.f5promotora.crm.resource.jpa.repository.ProfileRepo;
import br.com.f5promotora.crm.resource.r2dbc.criteria.ProfileCriteria;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

@Getter
@RequiredArgsConstructor
@Service("profileServiceImplV1")
public class ProfileServiceImpl implements ProfileService {

  private final CompanyService companyService;

  private final ProfileRepo repo;
  private final ProfileMapper mapper;

  private final DatabaseClient dbClient;
  private final R2dbcEntityTemplate r2dbcTemplate;

  private final PasswordEncoder passwordEncoder;

  @Override
  public Mono<Long> amount(ProfileFilter filter) {
    return r2dbcTemplate.count(
        Query.query(ProfileCriteria.execute(filter)),
        br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Profile.class);
  }

  @Override
  public Flux<ProfileDTO> filter(ProfileFilter filter, Pageable pageable) {
    return dbClient
        .select()
        .from(br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Profile.class)
        .matching(ProfileCriteria.execute(filter))
        .page(pageable)
        .fetch()
        .all()
        .flatMap(
            profile ->
                Mono.just(mapper.toDtoR2dbc(profile))
                    .flatMap(
                        profileDto -> {
                          if (profile.getCompanyId() != null) {
                            return companyService
                                .get(profile.getCompanyId())
                                .doOnNext(profileDto::setCompany)
                                .thenReturn(profileDto);
                          }
                          return Mono.just(profileDto);
                        }));
  }

  @Override
  public Mono<ImportResult<ProfileDTO, ProfileFormCreate>> save(Set<ProfileFormCreate> forms) {
    return Flux.fromIterable(forms)
        .parallel()
        .runOn(Schedulers.newParallel("save-profile", 3))
        .flatMap(
            form ->
                filter(
                        ProfileFilter.builder().setUsername(form.getUsername()).build(),
                        PageRequest.of(0, 1))
                    .next()
                    .flatMap(profile -> update(profile.getId(), form))
                    .switchIfEmpty(create(form))
                    .map(
                        profile ->
                            Pair.of(
                                Optional.of(profile),
                                Optional.<Pair<ProfileFormCreate, String>>empty()))
                    .onErrorResume(
                        ex ->
                            Mono.just(
                                Pair.of(
                                    Optional.<ProfileDTO>empty(),
                                    Optional.of(
                                        Pair.of(
                                            form, ((ResponseStatusException) ex).getReason()))))))
        .sequential()
        .collectList()
        .map(
            profiles ->
                ImportResult.<ProfileDTO, ProfileFormCreate>builder()
                    .setSuccess(
                        profiles.stream()
                            .map(Pair::getFirst)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toSet()))
                    .setFlaws(
                        profiles.stream()
                            .map(Pair::getSecond)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toSet()))
                    .build());
  }

  @Override
  public Mono<ProfileDTO> get(UUID id) {
    return filter(
            ProfileFilter.builder().setId(Collections.singleton(id)).build(), PageRequest.of(0, 1))
        .next()
        .switchIfEmpty(
            Mono.error(
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Profile with id " + id + " not found")));
  }

  @Override
  public Mono<ProfileDTO> create(ProfileFormCreate form) {
    return Mono.zip(emailInUse(form.getEmail()), usernameInUse(form.getUsername()))
        .doOnNext(
            zip -> {
              if (zip.getT1() || zip.getT2()) {
                StringBuilder str = new StringBuilder();
                if (zip.getT1()) {
                  str.append("Email");
                  if (zip.getT2()) {
                    str.append(" and Username");
                  }
                } else if (zip.getT2()) {
                  str.append("Username");
                }

                if (str.toString().length() != 0) {
                  throw new ResponseStatusException(
                      HttpStatus.CONFLICT, str.toString() + " in use");
                }
              }
            })
        .then(Mono.just(mapper.toEntity(form)))
        .doOnNext(profile -> profile.setStatus(ProfileStatus.INVITED))
        .doOnNext(profile -> profile.setPassword(passwordEncoder.encode(profile.getPassword())))
        .doOnNext(
            profile -> {
              if (profile.getPermissions() == null
                  || (profile.getPermissions() != null && profile.getPermissions().isEmpty())) {
                profile.setPermissions(Collections.singleton(ProfilePermission.VIEW));
              }

              if (profile.getRoles() == null
                  || (profile.getRoles() != null && profile.getRoles().isEmpty())) {
                profile.setRoles(Collections.singleton(ProfileRole.REGULAR));
              }
            })
        .flatMap(
            profile -> {
              if (form.getCompanyName() != null) {
                return setCompanyByName(profile, form.getCompanyName());
              }
              return Mono.just(profile);
            })
        .map(repo::save)
        .map(mapper::toDtoJpa);
  }

  @Override
  public Mono<ProfileDTO> update(UUID id, ProfileFormCreate form) {
    return Mono.justOrEmpty(repo.findById(id))
        .switchIfEmpty(
            Mono.error(
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Profile with id " + id + " not found")))
        .doOnNext(
            profile -> {
              if (form.getFirstName() != null && !form.getFirstName().isBlank()) {
                profile.setFirstName(form.getFirstName());
              }
              if (form.getSecondName() != null && !form.getSecondName().isBlank()) {
                profile.setSecondName(form.getSecondName());
              }
            })
        .doOnNext(
            profile -> {
              if (form.getPermissions() != null && !form.getPermissions().isEmpty()) {
                profile.setPermissions(form.getPermissions());
              }
              if (form.getRoles() != null && !form.getRoles().isEmpty()) {
                profile.setRoles(form.getRoles());
              }
            })
        .flatMap(
            profile -> {
              if (profile.getEmail() != null && !profile.getEmail().isBlank()) {
                return emailInUse(form.getEmail())
                    .doOnNext(
                        res -> {
                          if (!res) {
                            profile.setEmail(form.getEmail());
                          }
                        })
                    .thenReturn(profile);
              }
              return Mono.just(profile);
            })
        .flatMap(
            profile -> {
              if (form.getUsername() != null && !form.getUsername().isBlank()) {
                return usernameInUse(form.getUsername())
                    .doOnNext(
                        res -> {
                          if (!res) {
                            profile.setUsername(form.getSecondName());
                          }
                        })
                    .thenReturn(profile);
              }
              return Mono.just(profile);
            })
        .flatMap(
            profile -> {
              if (form.getCompanyName() != null && profile.getCompany() != null) {
                if (profile.getCompany() != null) {
                  if (!profile.getCompany().getName().equals(form.getCompanyName())) {
                    return setCompanyByName(profile, form.getCompanyName());
                  }
                } else {
                  return setCompanyByName(profile, form.getCompanyName());
                }
              }
              return Mono.just(profile);
            })
        .map(repo::save)
        .map(mapper::toDtoJpa);
  }

  private Mono<Profile> setCompanyByName(Profile profile, String companyName) {
    return companyService
        .filter(CompanyFilter.builder().setNameEquals(companyName).build(), PageRequest.of(0, 1))
        .next()
        .map(company -> Company.builder().setId(company.getId()).setName(company.getName()).build())
        .switchIfEmpty(
            companyService
                .create(CompanyFormCreate.builder().setName(companyName).build())
                .map(
                    company ->
                        Company.builder()
                            .setId(company.getId())
                            .setName(company.getName())
                            .build()))
        .doOnNext(company -> profile.setCompany(company))
        .thenReturn(profile);
  }

  @Override
  public Mono<Void> delete(UUID id) {
    return find(id)
        .then(
            dbClient
                .delete()
                .from(br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Profile.class)
                .matching(
                    ProfileCriteria.execute(
                        ProfileFilter.builder().setId(Collections.singleton(id)).build()))
                .fetch()
                .rowsUpdated())
        .flatMap(
            rowsUpdated -> {
              if (rowsUpdated != 0) {
                return Mono.empty();
              }
              return Mono.error(
                  new ResponseStatusException(
                      HttpStatus.BAD_GATEWAY, "It was not possible to remove the profile."));
            });
  }

  @Override
  public Mono<Boolean> emailInUse(String email) {
    return r2dbcTemplate.exists(
        Query.query(ProfileCriteria.execute(ProfileFilter.builder().setEmail(email).build())),
        br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Profile.class);
  }

  @Override
  public Mono<Boolean> usernameInUse(String username) {
    return r2dbcTemplate.exists(
        Query.query(ProfileCriteria.execute(ProfileFilter.builder().setUsername(username).build())),
        br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Profile.class);
  }

  @Override
  public Mono<Profile> find(UUID id) {
    return dbClient
        .select()
        .from(br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Profile.class)
        .matching(
            ProfileCriteria.execute(
                ProfileFilter.builder().setId(Collections.singleton(id)).build()))
        .fetch()
        .one()
        .map(mapper::copy)
        .switchIfEmpty(
            Mono.error(
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Profile with id '" + id + "' not found")));
  }

  @Override
  public Mono<ProfileDTO> changeStatus(UUID id, ProfileStatus status) {
    return find(id)
        .doOnNext(profile -> profile.setStatus(status))
        .map(repo::save)
        .map(mapper::toDtoJpa);
  }

  @Override
  public Mono<String> authJWT(AuthPassword auth) {
    return Mono.justOrEmpty(repo.findByUsername(auth.getUsername()))
        .flatMap(
            profile -> {
              if (passwordEncoder.matches(auth.getPassword(), profile.getPassword())) {
                return Mono.just(
                        JWT.create()
                            .withIssuer("f5_crm")
                            .withExpiresAt(
                                new Date(System.currentTimeMillis() + 21600000)) /*NOW + 6H*/
                            .withSubject(auth.getUsername())
                            .withClaim("name", profile.getFirstName())
                            .sign(Algorithm.HMAC256("secret")))
                    .onErrorMap(
                        ex ->
                            new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR, "JWT Creation Exception"))
                    .doFinally(
                        onFinally -> {
                          if (!onFinally.equals(SignalType.ON_ERROR)) {
                            profile.setLastLogin(LocalDateTime.now());
                          }
                        })
                    .flatMap(
                        jwt -> {
                          return Mono.just(repo.save(profile))
                              .thenReturn(jwt)
                              .onErrorMap(
                                  ex ->
                                      new ResponseStatusException(
                                          HttpStatus.INTERNAL_SERVER_ERROR,
                                          "SQL error, it was not possible to update the settings in the profile."));
                        });
              }
              throw new ResponseStatusException(
                  HttpStatus.BAD_REQUEST, "Invalid username or password");
            })
        .switchIfEmpty(
            Mono.error(
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Profile with username " + auth.getUsername() + " not found")));
  }
}
