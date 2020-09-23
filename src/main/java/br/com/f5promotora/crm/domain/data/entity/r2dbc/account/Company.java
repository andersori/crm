package br.com.f5promotora.crm.domain.data.entity.r2dbc.account;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@Table("company")
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
@EqualsAndHashCode(of = {"name"})
public class Company {

  @Id
  @Column("id")
  private UUID id;

  @Column("name")
  private String name;
}
