package br.com.f5promotora.crm.resource.r2dbc.repository;

import br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Profile;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ProfileReactiveRepo extends R2dbcRepository<Profile, UUID> {}
