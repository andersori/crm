package br.com.f5promotora.crm.domain.data.entity.jpa.deal;

import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
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
@Table(name = "lost", catalog = "crm")
@EqualsAndHashCode(of = {"id"})
public class Lost {

  @Id
  @Setter(value = AccessLevel.PRIVATE)
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(length = 500)
  private String description;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "deal_id", nullable = false, updatable = false)
  private Deal deal;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "lost_reason",
      joinColumns = @JoinColumn(name = "lost_id"),
      inverseJoinColumns = @JoinColumn(name = "reason_id"))
  private Set<ReasonToLost> reasons;
}
