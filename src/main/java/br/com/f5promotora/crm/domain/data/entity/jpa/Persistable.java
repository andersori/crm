package br.com.f5promotora.crm.domain.data.entity.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.PreUpdate;
import lombok.Getter;

@Getter
public class Persistable implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column(columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
  final LocalDateTime createdAt = LocalDateTime.now();

  @Column(columnDefinition = "TIMESTAMP")
  LocalDateTime updatedAt;

  @PreUpdate
  void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
