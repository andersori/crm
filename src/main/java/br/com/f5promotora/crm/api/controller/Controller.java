package br.com.f5promotora.crm.api.controller;

import br.com.f5promotora.crm.domain.data.v1.dto.ImportResult;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Controller<DTO, Filter, Form> {
  Flux<DTO> filter(
      Filter filter,
      Boolean isPaged,
      Integer page,
      Integer size,
      String[] propriedades,
      Direction direcao,
      ServerHttpResponse response);

  Mono<DTO> create(Form form);

  Mono<DTO> update(UUID id, Form form);

  Mono<ImportResult<DTO, Form>> save(Set<Form> forms);

  Mono<DTO> get(UUID id);

  Mono<Void> delete(UUID id);
}
