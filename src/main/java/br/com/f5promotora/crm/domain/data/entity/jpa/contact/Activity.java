package br.com.f5promotora.crm.domain.data.entity.jpa.contact;

import br.com.f5promotora.crm.domain.data.entity.jpa.Persistable;
import br.com.f5promotora.crm.domain.data.entity.jpa.deal.Deal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "activity", catalog = "crm")
@EqualsAndHashCode(
    callSuper = false,
    of = {"title", "id"})
public class Activity extends Persistable {
  private static final long serialVersionUID = 1L;

  @Id
  @Setter(value = AccessLevel.PRIVATE)
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(length = 300, nullable = false)
  private String title;

  @Column(columnDefinition = "DATE")
  private LocalDate finalDate;

  @Column(columnDefinition = "TIME")
  private LocalTime finalTime;

  @Column(columnDefinition = "DATE")
  private LocalDate initialDate;

  @Column(columnDefinition = "TIME")
  private LocalTime initialTime;

  @Column(length = 200)
  private String location;

  @Column(length = 1000)
  private String description;

  @ManyToOne
  @JoinColumn(name = "deal_id")
  private Deal deal;

  @ManyToOne(optional = false)
  @JoinColumn(name = "person_id", nullable = false)
  private Person person;
}
