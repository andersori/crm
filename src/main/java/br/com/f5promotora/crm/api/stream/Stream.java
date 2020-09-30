package br.com.f5promotora.crm.api.stream;

import java.util.Set;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;

public interface Stream<DTO, Filter, Form> {

  Flux<DTO> filter(
      Filter filter,
      Boolean isPaged,
      Integer page,
      Integer size,
      String[] properties,
      Direction direction,
      ServerHttpResponse response);

  Flux<DTO> save(Set<Form> forms);
}
