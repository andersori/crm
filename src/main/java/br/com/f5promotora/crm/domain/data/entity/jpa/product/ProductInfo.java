package br.com.f5promotora.crm.domain.data.entity.jpa.product;

import br.com.f5promotora.crm.domain.data.entity.jpa.Persistable;
import br.com.f5promotora.crm.domain.data.enums.InfoType;
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
@Table(name = "product_info", catalog = "crm")
@EqualsAndHashCode(
    callSuper = false,
    of = {"info"})
public class ProductInfo extends Persistable {
  private static final long serialVersionUID = 1L;

  @Id
  @Setter(value = AccessLevel.PRIVATE)
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(length = 500, nullable = false)
  private String info;

  @Enumerated(EnumType.STRING)
  @Column(length = 30, nullable = false)
  private InfoType type;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "product_id", updatable = false)
  private Product product;
}
