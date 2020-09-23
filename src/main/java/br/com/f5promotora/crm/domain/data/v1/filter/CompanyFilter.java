package br.com.f5promotora.crm.domain.data.v1.filter;

import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class CompanyFilter {
  private Set<UUID> id;
  private String nameLike;
  private String nameEquals;
}
