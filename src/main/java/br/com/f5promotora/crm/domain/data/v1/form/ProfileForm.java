package br.com.f5promotora.crm.domain.data.v1.form;

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
public class ProfileForm {
  private String email;
  private String username;
  private UUID companyId;
  private String firstName;
  private String secondName;
}
