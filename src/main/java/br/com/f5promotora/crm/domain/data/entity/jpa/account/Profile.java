package br.com.f5promotora.crm.domain.data.entity.jpa.account;

import br.com.f5promotora.crm.domain.data.entity.jpa.Persistable;
import br.com.f5promotora.crm.domain.data.entity.jpa.accompaniment.Campaign;
import br.com.f5promotora.crm.domain.data.enums.ProfilePermission;
import br.com.f5promotora.crm.domain.data.enums.ProfileStatus;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
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
@Builder(setterPrefix = "set")
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

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ProfilePermission permission;

  @ManyToMany(mappedBy = "participants")
  private Set<Team> teams;

  @OneToMany(mappedBy = "owner")
  private Set<Campaign> campaign;
}
