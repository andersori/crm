package br.com.f5promotora.crm.resource.r2dbc.criteria;

import br.com.f5promotora.crm.domain.data.entity.jpa.account.Profile_;
import br.com.f5promotora.crm.domain.data.v1.filter.ProfileFilter;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;

public class ProfileCriteria {
  public static CriteriaDefinition execute(ProfileFilter filter) {
    Criteria criteria = Criteria.empty();
    if (filter.getId() != null && !filter.getId().isEmpty()) {
      criteria.and(Criteria.where(Profile_.ID).in(filter.getId()));
    }
    return criteria;
  }
}
