package br.com.f5promotora.crm.domain.service;

import br.com.f5promotora.crm.domain.data.v1.dto.ProfileDTO;
import br.com.f5promotora.crm.domain.data.v1.filter.ProfileFilter;
import br.com.f5promotora.crm.domain.data.v1.form.ProfileForm;
import reactor.core.publisher.Mono;

public interface ProfileService extends Service<ProfileDTO, ProfileForm, ProfileFilter> {

  Mono<Boolean> emailInUse(String email);

  Mono<Boolean> usernameInUse(String username);
}
