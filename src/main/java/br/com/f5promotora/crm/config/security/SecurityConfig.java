package br.com.f5promotora.crm.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfig {

  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http.csrf()
        .disable()
        .authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)
        .authorizeExchange()
        .pathMatchers("/webjars/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
        .permitAll()
        .pathMatchers("/actuator/**")
        .permitAll()
        .pathMatchers(HttpMethod.OPTIONS)
        .permitAll()
        .pathMatchers(HttpMethod.POST, "/v1/profile", "/v1/profile/auth/**", "/v1/company")
        .permitAll()
        .pathMatchers(HttpMethod.GET, "/v1/company")
        .permitAll()
        .anyExchange()
        .authenticated()
        .and()
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
