package br.com.f5promotora.crm.domain.service;

import br.com.f5promotora.crm.domain.data.v1.dto.ImportResult;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Service<DTO, Form, Filter, EntityJPA> {
  Mono<Long> amount(Filter filter);

  Flux<DTO> filter(Filter filter, Pageable pageable);

  Mono<ImportResult<DTO, Form>> save(Set<Form> forms);

  Mono<DTO> get(UUID id);

  Mono<EntityJPA> find(UUID id);

  Mono<DTO> create(Form form);

  Mono<DTO> update(UUID id, Form form);

  Mono<Void> delete(UUID id);
}
