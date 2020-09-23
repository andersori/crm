package br.com.f5promotora.crm.domain.data.entity.r2dbc.contact;

import br.com.f5promotora.crm.domain.data.entity.r2dbc.Persistable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Table("person")
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class Person extends Persistable {
  private static final long serialVersionUID = 1L;

  private UUID id;

  private String name;

  @Column("profile_id")
  private UUID ownerId;

  @Column("organization_id")
  private UUID organizationId;

  @Column("company_id")
  private UUID companyId;
}
