package br.com.f5promotora.crm.domain.data.entity.jpa.contact;

import br.com.f5promotora.crm.domain.data.entity.jpa.Persistable;
import br.com.f5promotora.crm.domain.data.entity.jpa.account.Company;
import br.com.f5promotora.crm.domain.data.entity.jpa.account.Profile;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "person", catalog = "crm")
@EqualsAndHashCode(
    callSuper = false,
    of = {"name"})
public class Person extends Persistable {
  private static final long serialVersionUID = 1L;

  @Id
  @Setter(value = AccessLevel.PRIVATE)
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(nullable = false, length = 250)
  private String name;

  @ManyToOne(optional = false)
  @JoinColumn(name = "profile_id", nullable = false)
  private Profile owner;

  @ManyToOne
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @ManyToOne(optional = false)
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;

  @OneToMany(mappedBy = "person")
  private Set<File> files;

  @OneToMany(mappedBy = "person")
  private Set<Note> notes;

  @OneToMany(mappedBy = "person")
  private Set<Contact> contacts;
}
