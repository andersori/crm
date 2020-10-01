package br.com.f5promotora.crm.domain.data.v1.filter;

import br.com.f5promotora.crm.domain.data.enums.ProfileAuthority;
import br.com.f5promotora.crm.domain.data.enums.ProfileRole;
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
  private UUID companyId;
  private String firstNameEquals;
  private String firstNameLike;
  private String secondNameEquals;
  private String secondNameLike;
  private Set<ProfileStatus> status;
  private LocalDateTime lastLogin;
  private LocalDateTime lastLoginBefore;
  private LocalDateTime lastLoginAfter;
  private Set<ProfileRole> roles;
  private Set<ProfileAuthority> authorities;
}
