package br.com.f5promotora.crm.domain.data.v1.dto;

import br.com.f5promotora.crm.domain.data.entity.jpa.account.Company;
import br.com.f5promotora.crm.domain.data.enums.ProfilePermission;
import br.com.f5promotora.crm.domain.data.enums.ProfileStatus;
import java.time.LocalDateTime;
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
@EqualsAndHashCode(of = {"username"})
public class ProfileDTO {
  private UUID id;
  private String email;
  private String username;
  private Company company;
  private String firstName;
  private String secondName;
  private ProfileStatus status;
  private LocalDateTime lastLogin;
  private ProfilePermission permission;
}
