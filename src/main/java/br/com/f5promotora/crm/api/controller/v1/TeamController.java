package br.com.f5promotora.crm.api.controller.v1;

import br.com.f5promotora.crm.api.Commons;
import br.com.f5promotora.crm.api.controller.Controller;
import br.com.f5promotora.crm.domain.data.v1.dto.TeamDTO;
import br.com.f5promotora.crm.domain.data.v1.filter.TeamFilter;
import br.com.f5promotora.crm.domain.data.v1.form.TeamFormCreate;
import br.com.f5promotora.crm.domain.service.TeamService;
import br.com.f5promotora.crm.domain.service.v1.TeamServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/v1/team")
@RestController("teamControllerV1")
public class TeamController implements Controller<TeamDTO, TeamFilter, TeamFormCreate> {

  private final TeamService service;

  public TeamController(@Qualifier("teamServiceImplV1") TeamServiceImpl service) {
    this.service = service;
  }

  @Override
  @GetMapping
  @Operation(
      security = {@SecurityRequirement(name = "bearer-jwt"), @SecurityRequirement(name = "basic")})
  public Flux<TeamDTO> filter(TeamFilter filter,
      @RequestParam(required = false, defaultValue = "true") Boolean isPaged,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "id") String[] properties,
      @RequestParam(required = false, defaultValue = "ASC") Direction direction,
      ServerHttpResponse response) {
    if (isPaged) {
      return Commons.execute(
          service, filter, PageRequest.of(page, size, direction, properties), response);
    }
    return Commons.execute(service, filter, Pageable.unpaged(), response);
  }

  @Override
  @PostMapping
  @Operation(
      security = {@SecurityRequirement(name = "bearer-jwt"), @SecurityRequirement(name = "basic")})
  public Mono<TeamDTO> create(@Valid @RequestBody TeamFormCreate form) {
    return service.create(form);
  }

  @Override
  @PutMapping("/{id}")
  @Operation(
      security = {@SecurityRequirement(name = "bearer-jwt"), @SecurityRequirement(name = "basic")})
  public Mono<TeamDTO> update(@PathVariable UUID id, @Valid @RequestBody TeamFormCreate form) {
    return service.update(id, form);
  }

  @Override
  @PostMapping("/import")
  @Operation(
      security = {@SecurityRequirement(name = "bearer-jwt"), @SecurityRequirement(name = "basic")})
  public Flux<TeamDTO> save(@Valid @RequestBody Set<TeamFormCreate> forms) {
    return service.save(forms);
  }

  @Override
  @GetMapping("/{id}")
  @Operation(
      security = {@SecurityRequirement(name = "bearer-jwt"), @SecurityRequirement(name = "basic")})
  public Mono<TeamDTO> get(@PathVariable UUID id) {
    return service.get(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @Operation(
      security = {@SecurityRequirement(name = "bearer-jwt"), @SecurityRequirement(name = "basic")})
  public Mono<Void> delete(@PathVariable UUID id) {
    return service.delete(id);
  }
}
