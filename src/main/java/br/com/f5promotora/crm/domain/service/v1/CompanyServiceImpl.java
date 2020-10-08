package br.com.f5promotora.crm.domain.service.v1;

import br.com.f5promotora.crm.domain.data.entity.jpa.account.Company;
import br.com.f5promotora.crm.domain.data.v1.dto.CompanyDTO;
import br.com.f5promotora.crm.domain.data.v1.dto.ImportResult;
import br.com.f5promotora.crm.domain.data.v1.filter.CompanyFilter;
import br.com.f5promotora.crm.domain.data.v1.filter.ProfileFilter;
import br.com.f5promotora.crm.domain.data.v1.form.CompanyFormCreate;
import br.com.f5promotora.crm.domain.data.v1.mapper.CompanyMapper;
import br.com.f5promotora.crm.domain.service.CompanyService;
import br.com.f5promotora.crm.resource.jpa.repository.CompanyRepo;
import br.com.f5promotora.crm.resource.r2dbc.criteria.CompanyCriteria;
import br.com.f5promotora.crm.resource.r2dbc.criteria.ProfileCriteria;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Service("companyServiceImplV1")
public class CompanyServiceImpl implements CompanyService {

  private final CompanyRepo repo;
  private final DatabaseClient dbClient;
  private final R2dbcEntityTemplate r2dbcTemplate;

  private final CompanyMapper mapper;

  @Override
  public Mono<Long> amount(CompanyFilter filter) {
    return r2dbcTemplate.count(
        Query.query(CompanyCriteria.execute(filter)),
        br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Company.class);
  }

  @Override
  public Flux<CompanyDTO> filter(CompanyFilter filter, Pageable pageable) {
    return dbClient
        .select()
        .from(br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Company.class)
        .matching(CompanyCriteria.execute(filter))
        .page(pageable)
        .fetch()
        .all()
        .map(mapper::toDtoR2dbc);
  }

  @Override
  public Mono<ImportResult<CompanyDTO, CompanyFormCreate>> save(Set<CompanyFormCreate> forms) {
    return Flux.fromIterable(forms)
        .parallel()
        .runOn(Schedulers.newParallel("save", 3))
        .flatMap(
            form ->
                create(form)
                    .map(
                        company ->
                            Pair.of(
                                Optional.of(company),
                                Optional.<Pair<CompanyFormCreate, String>>empty()))
                    .onErrorResume(
                        ex ->
                            Mono.just(
                                Pair.of(
                                    Optional.<CompanyDTO>empty(),
                                    Optional.of(
                                        Pair.of(
                                            form, ((ResponseStatusException) ex).getReason()))))))
        .sequential()
        .collectList()
        .flatMap(
            companies ->
                Mono.just(new ImportResult<CompanyDTO, CompanyFormCreate>())
                    .doOnNext(
                        res -> {
                          res.setSuccess(
                              companies.stream()
                                  .map(Pair::getFirst)
                                  .filter(Optional::isPresent)
                                  .map(Optional::get)
                                  .collect(Collectors.toSet()));

                          res.setFlaws(
                              companies.stream()
                                  .map(Pair::getSecond)
                                  .filter(Optional::isPresent)
                                  .map(Optional::get)
                                  .collect(Collectors.toSet()));
                        }));
  }

  @Override
  public Mono<CompanyDTO> get(UUID id) {
    return find(id).map(mapper::toDtoJpa);
  }

  @Override
  public Mono<CompanyDTO> create(CompanyFormCreate form) {
    return nameInUse(form.getName())
        .doOnNext(
            exists -> {
              if (exists) {
                throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "The name '" + form.getName() + "' is already in use");
              }
            })
        .then(Mono.just(mapper.toEntity(form)))
        .map(repo::save)
        .map(mapper::toDtoJpa);
  }

  @Override
  public Mono<CompanyDTO> update(UUID id, CompanyFormCreate form) {
    return find(id)
        .flatMap(
            company -> {
              if (!company.getName().equals(form.getName())) {
                return nameInUse(form.getName())
                    .map(
                        exists -> {
                          if (exists) {
                            throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "The name '" + form.getName() + "' is already in use");
                          }
                          company.setName(form.getName());
                          return repo.save(company);
                        });
              }
              return Mono.just(company);
            })
        .map(mapper::toDtoJpa);
  }

  @Override
  public Mono<Void> delete(UUID id) {
    return find(id)
        .then(
            dbClient
                .delete()
                .from(br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Company.class)
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
  public Mono<Company> find(UUID id) {
    return dbClient
        .select()
        .from(br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Company.class)
        .matching(
            CompanyCriteria.execute(
                CompanyFilter.builder().setId(Collections.singleton(id)).build()))
        .fetch()
        .one()
        .map(mapper::copy)
        .switchIfEmpty(
            Mono.error(
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Company with id '" + id + "' not found")));
  }

  @Override
  public Mono<Boolean> nameInUse(String name) {
    return r2dbcTemplate.exists(
        Query.query(CompanyCriteria.execute(CompanyFilter.builder().setNameEquals(name).build())),
        br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Company.class);
  }
}
