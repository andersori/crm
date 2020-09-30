package br.com.f5promotora.crm.domain.data.entity.jpa.account;

import br.com.f5promotora.crm.domain.data.entity.jpa.Persistable;
import br.com.f5promotora.crm.domain.data.entity.jpa.contact.Person;
import br.com.f5promotora.crm.domain.data.entity.jpa.deal.ReasonToLost;
import br.com.f5promotora.crm.domain.data.entity.jpa.product.Product;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "company", catalog = "crm")
@EqualsAndHashCode(
    callSuper = false,
    of = {"name"})
public class Company extends Persistable {
  private static final long serialVersionUID = 1L;

  @Id
  @Setter(value = AccessLevel.PRIVATE)
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(nullable = false, unique = true, length = 300)
  private String name;

  @OneToMany(mappedBy = "company")
  private Set<Profile> profiles;

  @OneToMany(mappedBy = "company")
  private Set<Person> person;

  @OneToMany(mappedBy = "company")
  private Set<ReasonToLost> reasons;

  @OneToMany(mappedBy = "company")
  private Set<Product> products;
}
