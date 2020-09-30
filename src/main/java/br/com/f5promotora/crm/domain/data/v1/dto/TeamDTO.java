package br.com.f5promotora.crm.domain.data.v1.dto;

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
@EqualsAndHashCode(of = "name")
public class TeamDTO {
  private UUID id;
  private String name;
  private String description;
  private ProfileDTO owner;
}
