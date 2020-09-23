package br.com.f5promotora.crm.api.controller.v1;

import br.com.f5promotora.crm.api.controller.Controller;
import br.com.f5promotora.crm.domain.data.v1.dto.CompanyDTO;
import br.com.f5promotora.crm.domain.data.v1.filter.CompanyFilter;
import br.com.f5promotora.crm.domain.data.v1.form.CompanyForm;
import br.com.f5promotora.crm.domain.service.CompanyService;
import io.swagger.annotations.ApiParam;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RequestMapping("/api/v1/company")
@RestController("companyControllerV1")
public class CompanyController implements Controller<CompanyDTO, CompanyFilter, CompanyForm> {

  private final CompanyService service;

  @Override
  @GetMapping
  public Flux<CompanyDTO> filter(
      CompanyFilter filter,
      @RequestParam(required = false, defaultValue = "true") Boolean isPaged,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "id") @ApiParam(allowMultiple = true)
          String[] properties,
      @RequestParam(required = false, defaultValue = "ASC") Direction direction,
      @ApiIgnore ServerHttpResponse response) {
    return Mono.defer(
            () -> {
              if (isPaged) {
                return Mono.just(Pageable.unpaged());
              }
              return Mono.just(PageRequest.of(page, size, direction, properties));
            })
        .flatMapMany(
            pageable ->
                service
                    .amount(filter)
                    .doOnNext(
                        amount -> {
                          response.getHeaders().add("X-Amount", Long.toString(amount));
                          response
                              .getHeaders()
                              .add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "X-Amount");
                        })
                    .thenMany(
                        service.filter(filter, pageable).subscribeOn(Schedulers.boundedElastic())));
  }

  @Override
  @PostMapping
  public Mono<CompanyDTO> create(@RequestBody CompanyForm form) {
    return service.create(form);
  }

  @Override
  @PostMapping("/import")
  public Flux<CompanyDTO> save(@RequestBody Set<CompanyForm> forms) {
    return service.save(forms);
  }

  @Override
  @GetMapping("/{id}")
  public Mono<CompanyDTO> get(@PathVariable UUID id) {
    return service.get(id);
  }
}
