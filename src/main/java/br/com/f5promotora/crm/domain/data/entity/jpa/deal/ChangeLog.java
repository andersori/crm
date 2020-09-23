package br.com.f5promotora.crm.domain.data.entity.jpa.deal;

import br.com.f5promotora.crm.domain.data.entity.jpa.contact.Person;
import br.com.f5promotora.crm.domain.data.enums.ChangeType;
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
@Table(name = "changeLog", catalog = "crm")
@EqualsAndHashCode(of = {"id"})
public class ChangeLog {

  @Id
  @Setter(value = AccessLevel.PRIVATE)
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(length = 300, nullable = false)
  private String value;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private ChangeType type;

  @ManyToOne(optional = false)
  @JoinColumn(name = "person_id", nullable = false)
  private Person person;
}
