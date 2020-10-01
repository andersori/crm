package br.com.f5promotora.crm.domain.data.v1.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class CompanyFormCreate {
  @NotNull
  @NotBlank
  @Size(min = 1, max = 300, message = "The name must contain between 1 and 300 characters")
  private String name;
}
