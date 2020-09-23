package br.com.f5promotora.crm.resource.r2dbc.criteria;

import br.com.f5promotora.crm.domain.data.entity.jpa.account.Company_;
import br.com.f5promotora.crm.domain.data.v1.filter.CompanyFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;

@Slf4j
public class CompanyCriteria {
  public static CriteriaDefinition execute(CompanyFilter filter) {
    Criteria criteria = Criteria.empty();
    if (filter.getId() != null && !filter.getId().isEmpty()) {
      criteria = criteria.and(Criteria.where(Company_.ID).in(filter.getId()));
    }
    if (filter.getNameLike() != null && !filter.getNameLike().isEmpty()) {
      criteria = criteria.and(Criteria.where(Company_.NAME).like("%" + filter.getNameLike() + "%"));
    }
    if (filter.getNameEquals() != null && !filter.getNameEquals().isEmpty()) {
      criteria = criteria.and(Criteria.where(Company_.NAME).is(filter.getNameEquals()));
    }
    log.info(criteria.toString());
    return criteria;
  }
}
