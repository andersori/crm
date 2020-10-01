package br.com.f5promotora.crm.domain.data.v1.mapper;

import br.com.f5promotora.crm.domain.data.Mapper;
import br.com.f5promotora.crm.domain.data.entity.jpa.account.Profile;
import br.com.f5promotora.crm.domain.data.entity.jpa.account.Team;
import br.com.f5promotora.crm.domain.data.v1.dto.TeamDTO;
import br.com.f5promotora.crm.domain.data.v1.form.TeamFormCreate;

@org.mapstruct.Mapper(
    componentModel = "spring",
    uses = {CompanyMapper.class, ProfileMapper.class})
public interface TeamMapper
    extends Mapper<
        br.com.f5promotora.crm.domain.data.entity.jpa.account.Team,
        br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Team,
        TeamDTO,
        TeamFormCreate> {

  default Team toEntity(TeamFormCreate form, Profile profile) {
    Team team = toEntity(form);
    team.setOwner(profile);
    return team;
  }
}
