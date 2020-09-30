package br.com.f5promotora.crm.domain.data.v1.filter;

import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
@EqualsAndHashCode(of = "nameEquals")
public class TeamFilter {
  private Set<UUID> id;
  private String nameEquals;
  private String nameLike;
  private String descriptionLike;
  private Set<UUID> companyId;
  private UUID ownerId;
}
