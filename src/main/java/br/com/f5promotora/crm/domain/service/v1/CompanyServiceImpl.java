package br.com.f5promotora.crm.domain.service.v1;

import br.com.f5promotora.crm.domain.data.entity.jpa.account.Company;
import br.com.f5promotora.crm.domain.data.v1.dto.CompanyDTO;
import br.com.f5promotora.crm.domain.data.v1.filter.CompanyFilter;
import br.com.f5promotora.crm.domain.data.v1.form.CompanyForm;
import br.com.f5promotora.crm.domain.data.v1.mapper.CompanyMapper;
import br.com.f5promotora.crm.domain.service.CompanyService;
import br.com.f5promotora.crm.resource.jpa.repository.CompanyRepo;
import br.com.f5promotora.crm.resource.r2dbc.criteria.CompanyCriteria;
import br.com.f5promotora.crm.resource.r2dbc.repository.CompanyReactiveRepo;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service("companyServiceImplV1")
public class CompanyServiceImpl implements CompanyService {

  private final CompanyRepo repo;
  private final DatabaseClient dbClient;
  private final CompanyReactiveRepo reactiveRepo;
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
  public Flux<CompanyDTO> save(Set<CompanyForm> forms) {
    return null;
  }

  @Override
  public Mono<CompanyDTO> get(UUID id) {
    return reactiveRepo
        .findById(id)
        .map(mapper::toDtoR2dbc)
        .switchIfEmpty(
            Mono.error(
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Company with id " + id + " not found")));
  }

  @Override
  public Mono<CompanyDTO> create(CompanyForm form) {
    return nameInUse(form.getName())
        .doOnNext(
            res -> {
              if (res) {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "The name " + form.getName() + " is already in use");
              }
            })
        .then(Mono.just(mapper.toEntity(form)))
        .map(repo::save)
        .map(mapper::toDtoJpa);
  }

  @Override
  public Mono<CompanyDTO> update(UUID id, CompanyForm form) {
    return null;
  }

  @Override
  public Mono<Void> delete(UUID id) {
    return null;
  }

  @Override
  public Mono<Company> find(UUID id) {
    return dbClient
        .select()
        .from(br.com.f5promotora.crm.domain.data.entity.jpa.account.Company.class)
        .matching(
            CompanyCriteria.execute(
                CompanyFilter.builder().setId(Collections.singleton(id)).build()))
        .fetch()
        .one()
        .map(company -> Company.builder().setId(company.getId()).setName(company.getName()).build())
        .switchIfEmpty(
            Mono.error(
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Company with id " + id + " not found")));
  }

  @Override
  public Mono<Boolean> nameInUse(String name) {
    return r2dbcTemplate.exists(
        Query.query(CompanyCriteria.execute(CompanyFilter.builder().setNameEquals(name).build())),
        br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Company.class);
  }
}