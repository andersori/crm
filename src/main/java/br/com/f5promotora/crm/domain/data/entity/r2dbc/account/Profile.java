package br.com.f5promotora.crm.domain.data.entity.r2dbc.account;

import br.com.f5promotora.crm.domain.data.entity.r2dbc.Persistable;
import br.com.f5promotora.crm.domain.data.enums.ProfilePermission;
import br.com.f5promotora.crm.domain.data.enums.ProfileRole;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Table("profile")
@NoArgsConstructor
@AllArgsConstructor
public class Profile extends Persistable {
  private static final long serialVersionUID = 1L;

  @Id
  @Setter(value = AccessLevel.PRIVATE)
  private UUID id;

  private String email;

  private String username;

  private String password;

  @Column("company_id")
  private UUID companyId;

  @Column("first_name")
  private String firstName;

  @Column("second_name")
  private String secondName;

  private String status;

  @Column("last_login")
  private LocalDateTime lastLogin;

  private String roles;

  private String permissions;

  public void setRoles(Set<ProfileRole> roles) {
    this.roles =
        roles.stream()
            .map(ProfileRole::name)
            .reduce(
                "",
                (a, b) -> {
                  if (!a.isEmpty()) {
                    return a + ';' + b;
                  } else {
                    return b;
                  }
                });
  }

  public Set<ProfileRole> getRoles() {
    return Arrays.asList(roles.split(";")).stream()
        .map(
            role -> {
              try {
                return Optional.of(ProfileRole.valueOf(role));
              } catch (IllegalArgumentException e) {
                /* IGNORE */
              }
              return Optional.<ProfileRole>empty();
            })
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toSet());
  }

  public void setPermissions(Set<ProfilePermission> permissions) {
    this.permissions =
        permissions.stream()
            .map(ProfilePermission::name)
            .reduce(
                "",
                (a, b) -> {
                  if (!a.isEmpty()) {
                    return a + ';' + b;
                  } else {
                    return b;
                  }
                });
  }

  public Set<ProfilePermission> getPermissions() {
    return Arrays.asList(permissions.split(";")).stream()
        .map(
            authority -> {
              try {
                return Optional.of(ProfilePermission.valueOf(authority));
              } catch (IllegalArgumentException e) {
                /* IGNORE */
              }
              return Optional.<ProfilePermission>empty();
            })
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toSet());
  }

  @Builder(setterPrefix = "set")
  public Profile(
      UUID id,
      String email,
      String username,
      String password,
      UUID companyId,
      String firstName,
      String secondName,
      String status,
      LocalDateTime lastLogin,
      Set<ProfileRole> roles,
      Set<ProfilePermission> permissions) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.password = password;
    this.companyId = companyId;
    this.firstName = firstName;
    this.secondName = secondName;
    this.status = status;
    this.lastLogin = lastLogin;
    setRoles(roles);
    setPermissions(permissions);
  }
}
