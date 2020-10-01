package br.com.f5promotora.crm.resource.r2dbc.criteria;

import br.com.f5promotora.crm.config.database.r2dbc.UUIDToByteArrayConverter;
import br.com.f5promotora.crm.domain.data.entity.jpa.account.Profile_;
import br.com.f5promotora.crm.domain.data.v1.filter.ProfileFilter;
import java.util.stream.Collectors;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;

public class ProfileCriteria {
  public static CriteriaDefinition execute(ProfileFilter filter) {
    Criteria criteria = Criteria.empty();
    if (filter != null) {
      if (filter.getId() != null && !filter.getId().isEmpty()) {
        criteria =
            criteria.and(
                Criteria.where(Profile_.ID)
                    .in(
                        filter.getId().stream()
                            .map(UUIDToByteArrayConverter.INSTANCE::convert)
                            .collect(Collectors.toSet())));
      }
      if (filter.getAuthorities() != null && !filter.getAuthorities().isEmpty()) {
        criteria = criteria.and(Criteria.where("authorities").in(filter.getAuthorities()));
      }
      if (filter.getRoles() != null && !filter.getRoles().isEmpty()) {
        criteria = criteria.and(Criteria.where("roles").in(filter.getRoles()));
      }
      if (filter.getCompanyId() != null) {
        criteria = criteria.and(Criteria.where("company_id").is(filter.getCompanyId()));
      }
      if (filter.getEmail() != null) {
        criteria = criteria.and(Criteria.where("email").is(filter.getEmail()));
      }
      if (filter.getUsername() != null) {
        criteria = criteria.and(Criteria.where("username").is(filter.getUsername()));
      }
    }
    return criteria;
  }
}
