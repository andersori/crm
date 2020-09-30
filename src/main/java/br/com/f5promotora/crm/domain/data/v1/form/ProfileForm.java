package br.com.f5promotora.crm.domain.data.v1.form;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class ProfileForm {
  @NotBlank private String email;
  @NotBlank private String username;
  @NotBlank private String password;
  @NotNull private UUID companyId;
  @NotNull private String firstName;
  private String secondName;
}
