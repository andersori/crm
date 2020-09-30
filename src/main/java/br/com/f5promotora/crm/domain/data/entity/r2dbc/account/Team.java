package br.com.f5promotora.crm.domain.data.entity.r2dbc.account;

import br.com.f5promotora.crm.domain.data.entity.r2dbc.Persistable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Table("team")
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class Team extends Persistable {
  private static final long serialVersionUID = 1L;

  @Id
  @Setter(value = AccessLevel.PRIVATE)
  private UUID id;

  private String name;

  @Column("profile_id")
  private UUID ownerId;

  private String description;
}
