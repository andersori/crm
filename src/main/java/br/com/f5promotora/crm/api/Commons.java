package br.com.f5promotora.crm.api;

import br.com.f5promotora.crm.domain.service.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Commons {

  public static <DTO, Form, FilterType, EntityJPA> Flux<DTO> execute(
      Service<DTO, Form, FilterType, EntityJPA> service,
      FilterType filter,
      Pageable pageable,
      ServerHttpResponse response) {
    return service
        .amount(filter)
        .doOnNext(
            amount -> {
              response.getHeaders().add("x-amount", Long.toString(amount));
              response.getHeaders().add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "x-amount");
            })
        .thenMany(service.filter(filter, pageable).subscribeOn(Schedulers.boundedElastic()));
  }
}
