package br.com.f5promotora.crm.domain.data.entity.jpa.account;

import br.com.f5promotora.crm.domain.data.entity.jpa.Persistable;
import br.com.f5promotora.crm.domain.data.entity.jpa.accompaniment.Campaign;
import br.com.f5promotora.crm.domain.data.enums.ProfileAuthority;
import br.com.f5promotora.crm.domain.data.enums.ProfileRole;
import br.com.f5promotora.crm.domain.data.enums.ProfileStatus;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile", catalog = "crm")
@EqualsAndHashCode(
    callSuper = false,
    of = {"username"})
public class Profile extends Persistable {
  private static final long serialVersionUID = 1L;

  @Id
  @Setter(value = AccessLevel.PRIVATE)
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, length = 200)
  private String password;

  @ManyToOne(optional = false)
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "second_name")
  private String secondName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ProfileStatus status;

  @Column(name = "last_login")
  private LocalDateTime lastLogin;

  @Column(nullable = false, length = 300)
  private String roles;

  @Column(nullable = false, length = 300)
  private String authorities;

  @OneToMany
  @JoinColumn(name = "manager")
  private Set<Team> managedTeams;

  @ManyToMany(mappedBy = "participants")
  private Set<Team> teams;

  @OneToMany(mappedBy = "owner")
  private Set<Campaign> campaign;

  public void setRoles(Set<ProfileRole> roles) {
    this.roles = roles.stream().map(ProfileRole::name).reduce("", (a, b) -> a + ';' + b);
  }

  public Set<ProfileRole> getRoles() {
    return Arrays.asList(roles.split(";")).stream()
        .map(
            role -> {
              try {
                return Optional.of(ProfileRole.valueOf(role));
              } catch (IllegalArgumentException e) {
                /*IGNORE*/
              }
              return Optional.<ProfileRole>empty();
            })
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toSet());
  }

  public void setAuthorities(Set<ProfileAuthority> authorities) {
    this.authorities =
        authorities.stream().map(ProfileAuthority::name).reduce("", (a, b) -> a + ';' + b);
  }

  public Set<ProfileAuthority> getAuthorities() {
    return Arrays.asList(authorities.split(";")).stream()
        .map(
            authority -> {
              try {
                return Optional.of(ProfileAuthority.valueOf(authority));
              } catch (IllegalArgumentException e) {
                /*IGNORE*/
              }
              return Optional.<ProfileAuthority>empty();
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
      Company company,
      String firstName,
      String secondName,
      ProfileStatus status,
      LocalDateTime lastLogin,
      Set<ProfileRole> roles,
      Set<ProfileAuthority> authorities,
      Set<Team> managedTeams,
      Set<Team> teams,
      Set<Campaign> campaign) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.password = password;
    this.company = company;
    this.firstName = firstName;
    this.secondName = secondName;
    this.status = status;
    this.lastLogin = lastLogin;
    setRoles(roles);
    setAuthorities(authorities);
    this.managedTeams = managedTeams;
    this.teams = teams;
    this.campaign = campaign;
  }
}
