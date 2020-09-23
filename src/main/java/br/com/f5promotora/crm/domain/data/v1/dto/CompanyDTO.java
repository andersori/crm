package br.com.f5promotora.crm.domain.data.v1.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder(setterPrefix = "set")
@EqualsAndHashCode(of = {"name"})
public class CompanyDTO {
  private UUID id;
  private String name;
}
