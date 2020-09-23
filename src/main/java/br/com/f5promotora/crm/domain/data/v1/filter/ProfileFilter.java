package br.com.f5promotora.crm.domain.data.v1.filter;

import br.com.f5promotora.crm.domain.data.entity.jpa.account.Company;
import br.com.f5promotora.crm.domain.data.enums.ProfilePermission;
import br.com.f5promotora.crm.domain.data.enums.ProfileStatus;
import java.time.LocalDateTime;
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
public class ProfileFilter {
  private Set<UUID> id;
  private String email;
  private String username;
  private Company company;
  private String firstName;
  private String secondName;
  private ProfileStatus status;
  private LocalDateTime lastLogin;
  private ProfilePermission permission;
}
