package br.com.f5promotora.crm.domain.service.v1;

import br.com.f5promotora.crm.domain.data.entity.jpa.account.Team;
import br.com.f5promotora.crm.domain.data.v1.dto.TeamDTO;
import br.com.f5promotora.crm.domain.data.v1.filter.TeamFilter;
import br.com.f5promotora.crm.domain.data.v1.form.TeamForm;
import br.com.f5promotora.crm.domain.data.v1.mapper.TeamMapper;
import br.com.f5promotora.crm.domain.service.ProfileService;
import br.com.f5promotora.crm.domain.service.TeamService;
import br.com.f5promotora.crm.resource.jpa.repository.TeamRepo;
import br.com.f5promotora.crm.resource.r2dbc.criteria.TeamCriteria;
import br.com.f5promotora.crm.resource.r2dbc.repository.TeamReactiveRepo;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
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
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Service("teamServiceImplV1")
public class TeamServiceImpl implements TeamService {

  private final ProfileService profileService;

  private final DatabaseClient dbClient;
  private final R2dbcEntityTemplate r2dbcTemplate;

  private final TeamRepo repo;
  private final TeamReactiveRepo reactiveRepo;

  private final TeamMapper mapper;

  @Override
  public Mono<Long> amount(TeamFilter filter) {
    return r2dbcTemplate.count(
        Query.query(TeamCriteria.execute(filter)),
        br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Team.class);
  }

  @Override
  public Flux<TeamDTO> filter(TeamFilter filter, Pageable pageable) {
    return dbClient
        .select()
        .from(br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Team.class)
        .matching(TeamCriteria.execute(filter))
        .fetch()
        .all()
        .flatMap(
            team ->
                Mono.just(mapper.toDtoR2dbc(team))
                    .flatMap(
                        teamDTO ->
                            profileService
                                .get(team.getOwnerId())
                                .doOnNext(profile -> teamDTO.setOwner(profile))
                                .thenReturn(teamDTO)));
  }

  @Override
  public Flux<TeamDTO> save(Set<TeamForm> forms) {
    return Flux.fromIterable(forms)
        .parallel()
        .runOn(Schedulers.newParallel("save", 10))
        .flatMap(
            form ->
                filter(
                        TeamFilter.builder()
                            .setOwnerId(form.getOwnerId())
                            .setNameEquals(form.getName())
                            .build(),
                        PageRequest.of(0, 1))
                    .collectList()
                    .flatMap(
                        teams -> {
                          if (teams.isEmpty()) {
                            return create(form);
                          }
                          return update(teams.get(0).getId(), form);
                        }))
        .sequential();
  }

  @Override
  public Mono<TeamDTO> get(UUID id) {
    return filter(
            TeamFilter.builder().setId(Collections.singleton(id)).build(), PageRequest.of(0, 1))
        .collectList()
        .map(
            teams -> {
              if (teams.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Team with id '" + id + "' not found");
              }
              return teams.get(0);
            });
  }

  @Override
  public Mono<TeamDTO> create(TeamForm form) {
    return profileService
        .find(form.getOwnerId())
        .map(profile -> mapper.toEntity(form, profile))
        .map(repo::save)
        .map(mapper::toDtoJpa);
  }

  @Override
  public Mono<TeamDTO> update(UUID id, TeamForm form) {

    return null;
  }

  @Override
  public Mono<Void> delete(UUID id) {
    return find(id).then(reactiveRepo.deleteById(id));
  }

  @Override
  public Mono<Team> find(UUID id) {
    return dbClient
        .select()
        .from(br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Team.class)
        .matching(
            TeamCriteria.execute(TeamFilter.builder().setId(Collections.singleton(id)).build()))
        .fetch()
        .one()
        .map(mapper::copy)
        .switchIfEmpty(
            Mono.error(
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Team with id '" + id + "' not found")));
  }
}
