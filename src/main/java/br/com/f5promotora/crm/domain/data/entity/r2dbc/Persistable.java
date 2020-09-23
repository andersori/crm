package br.com.f5promotora.crm.domain.data.entity.r2dbc;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;

@Getter
public class Persistable implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column("created_at")
  LocalDateTime createdAt;

  @Column("updated_at")
  LocalDateTime updatedAt;
}
