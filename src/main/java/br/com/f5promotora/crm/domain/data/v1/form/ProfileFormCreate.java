package br.com.f5promotora.crm.domain.data.v1.form;

import br.com.f5promotora.crm.domain.data.enums.ProfilePermission;
import br.com.f5promotora.crm.domain.data.enums.ProfileRole;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
public class ProfileFormCreate {
  @NotBlank private String email;
  @NotBlank private String username;
  @NotBlank private String password;
  @NotNull private String firstName;
  private String companyName;
  private String secondName;
  @NotEmpty private Set<ProfileRole> roles;
  @NotEmpty private Set<ProfilePermission> permissions;
}
