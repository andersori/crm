package br.com.f5promotora.crm.resource.r2dbc.repository;

import br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Team;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface TeamReactiveRepo extends R2dbcRepository<Team, UUID> {}
