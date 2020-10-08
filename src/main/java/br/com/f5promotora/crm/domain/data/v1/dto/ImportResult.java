package br.com.f5promotora.crm.domain.data.v1.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class ImportResult<DTO, Form> {
  private Set<DTO> success;
  private Set<Pair<Form, String>> flaws;

  public void addSuccess(DTO dto) {
    if (success == null) {
      success = new HashSet<>();
    }
    success.add(dto);
  }

  public void addFailure(Pair<Form, String> data) {
    if (flaws == null) {
      flaws = new HashSet<>();
    }
    flaws.add(data);
  }
}
