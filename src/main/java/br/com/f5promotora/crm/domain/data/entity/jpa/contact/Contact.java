package br.com.f5promotora.crm.domain.data.entity.jpa.contact;

import br.com.f5promotora.crm.domain.data.enums.ContactType;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "contact", catalog = "crm")
@EqualsAndHashCode(
    callSuper = false,
    of = {"value"})
public class Contact {

  @Id
  @Setter(value = AccessLevel.PRIVATE)
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(nullable = false, length = 200)
  private String value;

  @ManyToOne(optional = false)
  @JoinColumn(name = "person_id", nullable = false)
  private Person person;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 15)
  private ContactType type;
}
