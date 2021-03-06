package br.com.f5promotora.crm.domain.service;

import br.com.f5promotora.crm.domain.data.entity.jpa.account.Company;
import br.com.f5promotora.crm.domain.data.v1.dto.CompanyDTO;
import br.com.f5promotora.crm.domain.data.v1.filter.CompanyFilter;
import br.com.f5promotora.crm.domain.data.v1.form.CompanyFormCreate;
import reactor.core.publisher.Mono;

public interface CompanyService
    extends Service<CompanyDTO, CompanyFormCreate, CompanyFilter, Company> {

  Mono<Boolean> nameInUse(String name);
}
