package br.com.f5promotora.crm.resource.r2dbc.repository;

import br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Company;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CompanyReactiveRepo extends R2dbcRepository<Company, UUID> {}
