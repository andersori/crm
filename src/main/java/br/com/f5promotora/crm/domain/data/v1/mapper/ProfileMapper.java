package br.com.f5promotora.crm.domain.data.v1.mapper;

import br.com.f5promotora.crm.domain.data.Mapper;
import br.com.f5promotora.crm.domain.data.entity.jpa.account.Profile;
import br.com.f5promotora.crm.domain.data.v1.dto.ProfileDTO;
import br.com.f5promotora.crm.domain.data.v1.form.ProfileFormCreate;
import org.mapstruct.Mapping;

@org.mapstruct.Mapper(
    componentModel = "spring",
    uses = {CompanyMapper.class})
public interface ProfileMapper
    extends Mapper<
        br.com.f5promotora.crm.domain.data.entity.jpa.account.Profile,
        br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Profile,
        ProfileDTO,
        ProfileFormCreate> {

  @Override
  @Mapping(target = "company.id", source = "companyId")
  Profile copy(br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Profile entity);
}
