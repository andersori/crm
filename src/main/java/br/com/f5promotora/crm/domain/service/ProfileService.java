package br.com.f5promotora.crm.domain.service;

import br.com.f5promotora.crm.domain.data.entity.jpa.account.Profile;
import br.com.f5promotora.crm.domain.data.enums.ProfileStatus;
import br.com.f5promotora.crm.domain.data.v1.dto.ProfileDTO;
import br.com.f5promotora.crm.domain.data.v1.filter.ProfileFilter;
import br.com.f5promotora.crm.domain.data.v1.form.AuthPassword;
import br.com.f5promotora.crm.domain.data.v1.form.ProfileFormCreate;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ProfileService
    extends Service<ProfileDTO, ProfileFormCreate, ProfileFilter, Profile> {

  Mono<Boolean> emailInUse(String email);

  Mono<Boolean> usernameInUse(String username);

  Mono<ProfileDTO> changeStatus(UUID id, ProfileStatus status);

  Mono<String> authJWT(AuthPassword auth);
}
