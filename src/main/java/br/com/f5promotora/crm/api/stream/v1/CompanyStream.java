package br.com.f5promotora.crm.api.stream.v1;

import br.com.f5promotora.crm.api.Commons;
import br.com.f5promotora.crm.api.stream.Stream;
import br.com.f5promotora.crm.domain.data.v1.dto.CompanyDTO;
import br.com.f5promotora.crm.domain.data.v1.filter.CompanyFilter;
import br.com.f5promotora.crm.domain.data.v1.form.CompanyForm;
import br.com.f5promotora.crm.domain.service.CompanyService;
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

@RequestMapping("/stream/v1/company")
@RestController("companyStreamControllerV1")
public class CompanyStream implements Stream<CompanyDTO, CompanyFilter, CompanyForm> {

  private final CompanyService service;

  public CompanyStream(@Qualifier("companyServiceImplV1") CompanyService service) {
    this.service = service;
  }

  @Override
  @GetMapping(produces = {MediaType.APPLICATION_STREAM_JSON_VALUE})
  public Flux<CompanyDTO> filter(
      @RequestParam(required = false) CompanyFilter filter,
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
  public Flux<CompanyDTO> save(@Valid @RequestBody Set<CompanyForm> forms) {
    return service.save(forms);
  }
}
