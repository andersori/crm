package br.com.f5promotora.crm.resource.r2dbc.criteria;

import br.com.f5promotora.crm.config.database.r2dbc.UUIDToByteArrayConverter;
import br.com.f5promotora.crm.domain.data.entity.jpa.account.Team_;
import br.com.f5promotora.crm.domain.data.v1.filter.TeamFilter;
import java.util.stream.Collectors;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;

public class TeamCriteria {
  public static CriteriaDefinition execute(TeamFilter filter) {
    Criteria criteria = Criteria.empty();
    if (filter != null) {
      if (filter.getId() != null && !filter.getId().isEmpty()) {
        criteria =
            criteria.and(
                Criteria.where(Team_.ID)
                    .in(
                        filter.getId().stream()
                            .map(UUIDToByteArrayConverter.INSTANCE::convert)
                            .collect(Collectors.toSet())));
      }
    }
    return criteria;
  }
}
