package br.com.f5promotora.crm.resource.jpa.specification;

import br.com.f5promotora.crm.domain.data.entity.jpa.account.Profile;
import br.com.f5promotora.crm.domain.data.v1.filter.ProfileFilter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class ProfileSpecification implements Specification<Profile> {
  private static final long serialVersionUID = 1L;

  private final ProfileFilter filter;

  @Override
  public Predicate toPredicate(
      Root<Profile> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicates = new ArrayList<Predicate>();
    if (filter.getId() != null && !filter.getId().isEmpty()) {}

    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
  }
}
