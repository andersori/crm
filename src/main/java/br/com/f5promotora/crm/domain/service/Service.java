package br.com.f5promotora.crm.domain.service;

import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Service<DTO, Form, Filter> {
  Mono<Long> amount(Filter filter);

  Flux<DTO> filter(Filter filter, Pageable pageable);

  Flux<DTO> save(Set<Form> forms);

  Mono<DTO> get(UUID id);

  Mono<DTO> create(Form form);

  Mono<DTO> update(UUID id, Form form);

  Mono<Void> delete(UUID id);
}
