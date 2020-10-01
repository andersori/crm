package br.com.f5promotora.crm.domain.data.v1.mapper;

import br.com.f5promotora.crm.domain.data.Mapper;
import br.com.f5promotora.crm.domain.data.v1.dto.CompanyDTO;
import br.com.f5promotora.crm.domain.data.v1.form.CompanyFormCreate;

@org.mapstruct.Mapper(componentModel = "spring")
public interface CompanyMapper
    extends Mapper<
        br.com.f5promotora.crm.domain.data.entity.jpa.account.Company,
        br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Company,
        CompanyDTO,
        CompanyFormCreate> {}
