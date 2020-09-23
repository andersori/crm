package br.com.f5promotora.crm.resource.jpa.repository;

import br.com.f5promotora.crm.domain.data.entity.jpa.deal.Lost;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostRepo extends JpaRepository<Lost, UUID> {}
