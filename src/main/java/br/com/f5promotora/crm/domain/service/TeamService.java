package br.com.f5promotora.crm.domain.service;

import br.com.f5promotora.crm.domain.data.entity.jpa.account.Team;
import br.com.f5promotora.crm.domain.data.v1.dto.TeamDTO;
import br.com.f5promotora.crm.domain.data.v1.filter.TeamFilter;
import br.com.f5promotora.crm.domain.data.v1.form.TeamFormCreate;

public interface TeamService extends Service<TeamDTO, TeamFormCreate, TeamFilter, Team> {}
