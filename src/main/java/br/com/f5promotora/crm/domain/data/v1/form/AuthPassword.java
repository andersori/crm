package br.com.f5promotora.crm.domain.data.v1.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class AuthPassword {
  @NotNull @NotEmpty private String username;
  @NotNull @NotEmpty private String password;
}
