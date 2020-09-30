package br.com.f5promotora.crm.domain.data.entity.r2dbc.account;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("team_participant")
@Builder(setterPrefix = "set")
public class TeamCampaign {

  @Column("team_id")
  private UUID teamId;

  @Column("profile_id")
  private UUID participantId;
}
