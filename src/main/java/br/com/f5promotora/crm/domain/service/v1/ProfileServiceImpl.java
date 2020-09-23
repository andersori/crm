package br.com.f5promotora.crm.domain.service.v1;

import br.com.f5promotora.crm.domain.data.v1.dto.ProfileDTO;
import br.com.f5promotora.crm.domain.data.v1.filter.ProfileFilter;
import br.com.f5promotora.crm.domain.data.v1.form.ProfileForm;
import br.com.f5promotora.crm.domain.data.v1.mapper.ProfileMapper;
import br.com.f5promotora.crm.domain.service.CompanyService;
import br.com.f5promotora.crm.domain.service.ProfileService;
import br.com.f5promotora.crm.resource.jpa.repository.ProfileRepo;
import br.com.f5promotora.crm.resource.r2dbc.criteria.ProfileCriteria;
import br.com.f5promotora.crm.resource.r2dbc.repository.ProfileReactiveRepo;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Getter
@RequiredArgsConstructor
@Service("profileServiceImplV1")
public class ProfileServiceImpl implements ProfileService {

  private final CompanyService companyService;

  private final ProfileRepo repo;
  private final ProfileReactiveRepo reactiveRepo;

  private final DatabaseClient dbClient;
  private final R2dbcEntityTemplate r2dbcTemplate;

  private final ProfileMapper mapper;

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
        .map(mapper::toDtoR2dbc);
  }

  @Override
  public Flux<ProfileDTO> save(Set<ProfileForm> forms) {
    return Flux.fromIterable(forms)
        .flatMap(
            form ->
                filter(
                        ProfileFilter.builder().setUsername(form.getUsername()).build(),
                        PageRequest.of(0, 1))
                    .collectList()
                    .flatMap(
                        profiles -> {
                          if (profiles.isEmpty()) {
                            return create(form);
                          }
                          return update(profiles.get(0).getId(), form);
                        }));
  }

  @Override
  public Mono<ProfileDTO> get(UUID id) {
    return filter(
            ProfileFilter.builder().setId(Collections.singleton(id)).build(), PageRequest.of(0, 1))
        .collectList()
        .map(
            profiles -> {
              if (profiles.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Profile with id " + id + " not found");
              }
              return profiles.get(0);
            });
  }

  @Override
  public Mono<ProfileDTO> create(ProfileForm form) {
    return companyService
        .find(form.getCompanyId())
        .flatMap(
            company ->
                Mono.zip(emailInUse(form.getEmail()), usernameInUse(form.getUsername()))
                    .doOnNext(
                        res -> {
                          if (res.getT1()) {
                            throw new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "O email " + form.getEmail() + " está em uso");
                          }
                          if (res.getT2()) {
                            throw new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "O username " + form.getUsername() + " está em uso");
                          }
                        })
                    .then(Mono.just(mapper.toEntity(form)))
                    .doOnNext(profile -> profile.setCompany(company))
                    .map(repo::save)
                    .map(mapper::toDtoJpa));
  }

  @Override
  public Mono<ProfileDTO> update(UUID id, ProfileForm form) {
    return Mono.defer(
            () -> {
              Optional<br.com.f5promotora.crm.domain.data.entity.jpa.account.Profile> profile =
                  repo.findById(id);

              if (profile.isPresent()) {
                return Mono.just(profile.get());
              }
              return Mono.empty();
            })
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
                usernameInUse(form.getUsername())
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
              if (form.getCompanyId() != null) {
                if (!profile.getCompany().getId().equals(form.getCompanyId())) {
                  return companyService
                      .find(id)
                      .doOnNext(
                          company -> {
                            profile.setCompany(company);
                          })
                      .thenReturn(profile);
                }
              }
              return Mono.just(profile);
            })
        .map(repo::save)
        .map(mapper::toDtoJpa);
  }

  @Override
  public Mono<Void> delete(UUID id) {
    return null;
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
}
