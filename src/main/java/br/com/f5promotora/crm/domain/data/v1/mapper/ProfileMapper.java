package br.com.f5promotora.crm.domain.data.v1.mapper;

import br.com.f5promotora.crm.domain.data.Mapper;
import br.com.f5promotora.crm.domain.data.v1.dto.ProfileDTO;
import br.com.f5promotora.crm.domain.data.v1.form.ProfileForm;

@org.mapstruct.Mapper(componentModel = "spring")
public interface ProfileMapper
    extends Mapper<
        br.com.f5promotora.crm.domain.data.entity.jpa.account.Profile,
        br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Profile,
        ProfileDTO,
        ProfileForm> {}
