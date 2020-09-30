package br.com.f5promotora.crm.domain.data.entity.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import lombok.Getter;

@Getter
@MappedSuperclass
public class Persistable implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
  final LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
  LocalDateTime updatedAt;

  @PreUpdate
  void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
