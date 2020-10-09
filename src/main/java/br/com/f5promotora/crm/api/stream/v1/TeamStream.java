package br.com.f5promotora.crm.api.stream.v1;

import br.com.f5promotora.crm.api.Commons;
import br.com.f5promotora.crm.api.stream.Stream;
import br.com.f5promotora.crm.domain.data.v1.dto.TeamDTO;
import br.com.f5promotora.crm.domain.data.v1.filter.TeamFilter;
import br.com.f5promotora.crm.domain.data.v1.form.TeamFormCreate;
import br.com.f5promotora.crm.domain.service.TeamService;
import br.com.f5promotora.crm.domain.service.v1.account.TeamServiceImpl;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequestMapping("/stream/v1/team")
@RestController("teamStreamV1")
public class TeamStream implements Stream<TeamDTO, TeamFilter, TeamFormCreate> {

  private final TeamService service;

  public TeamStream(@Qualifier("teamServiceImplV1") TeamServiceImpl service) {
    this.service = service;
  }

  @Override
  @GetMapping(produces = {MediaType.APPLICATION_STREAM_JSON_VALUE})
  public Flux<TeamDTO> filter(
      @RequestParam(required = false) TeamFilter filter,
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
  @PostMapping(
      value = "/import",
      produces = {MediaType.APPLICATION_STREAM_JSON_VALUE})
  public Flux<TeamDTO> save(@Valid @RequestBody Set<TeamFormCreate> forms) {
    return service.save(forms).flatMapMany(res -> Flux.fromIterable(res.getSuccess()));
  }
}
