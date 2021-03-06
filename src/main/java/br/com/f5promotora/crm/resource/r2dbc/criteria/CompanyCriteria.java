package br.com.f5promotora.crm.resource.r2dbc.criteria;

import br.com.f5promotora.crm.config.database.r2dbc.UUIDToByteArrayConverter;
import br.com.f5promotora.crm.domain.data.entity.jpa.account.Company_;
import br.com.f5promotora.crm.domain.data.v1.filter.CompanyFilter;
import java.util.stream.Collectors;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;

public class CompanyCriteria {
  public static CriteriaDefinition execute(CompanyFilter filter) {
    Criteria criteria = Criteria.empty();
    if (filter != null) {
      if (filter.getId() != null && !filter.getId().isEmpty()) {
        criteria =
            criteria.and(
                Criteria.where(Company_.ID)
                    .in(
                        filter.getId().stream()
                            .map(UUIDToByteArrayConverter.INSTANCE::convert)
                            .collect(Collectors.toSet())));
      }
      if (filter.getNameLike() != null && !filter.getNameLike().isEmpty()) {
        criteria =
            criteria.and(Criteria.where(Company_.NAME).like("%" + filter.getNameLike() + "%"));
      }
      if (filter.getNameEquals() != null && !filter.getNameEquals().isEmpty()) {
        criteria = criteria.and(Criteria.where(Company_.NAME).is(filter.getNameEquals()));
      }
    }
    return criteria;
  }
}
