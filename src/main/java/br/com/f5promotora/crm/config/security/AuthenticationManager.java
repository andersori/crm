package br.com.f5promotora.crm.config.security;

import br.com.f5promotora.crm.domain.data.entity.r2dbc.account.Profile;
import br.com.f5promotora.crm.domain.data.enums.ProfileAuthority;
import br.com.f5promotora.crm.domain.data.v1.filter.ProfileFilter;
import br.com.f5promotora.crm.resource.r2dbc.criteria.ProfileCriteria;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

  private Scheduler scheduler = Schedulers.boundedElastic();
  private JWTVerifier verifier =
      JWT.require(Algorithm.HMAC256("secret")).withIssuer("auth0").build();
  private final DatabaseClient db;
  private final PasswordEncoder passwordEncoder;

  public AuthenticationManager(DatabaseClient db, @Lazy PasswordEncoder passwordEncoder) {
    this.db = db;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    return Mono.defer(
            () -> {
              if (authentication.getName().equalsIgnoreCase(AuthType.BASIC.name())) {
                return Mono.just(AuthType.BASIC);
              } else if (authentication.getName().equalsIgnoreCase(AuthType.BEARER.name())) {
                return Mono.just(AuthType.BEARER);
              }
              return Mono.just(AuthType.UNDEFINED);
            })
        .publishOn(scheduler)
        .flatMap(
            authType -> {
              String credentials = (String) authentication.getCredentials();

              if (authType.equals(AuthType.BASIC)) {
                String userPass =
                    new String(Base64.getDecoder().decode(credentials), StandardCharsets.UTF_8);

                if (userPass.split(":").length == 2) {
                  String username = userPass.split(":")[0].trim();
                  String password = userPass.split(":")[1].trim();

                  return findByUsername(username)
                      .map(
                          profile -> {
                            if (passwordEncoder.matches(password, profile.getPassword())) {
                              return new UsernamePasswordAuthenticationToken(
                                  profile,
                                  password,
                                  profile.getAuthorities().stream()
                                      .map(ProfileAuthority::name)
                                      .map(
                                          autority -> {
                                            return new GrantedAuthority() {
                                              private static final long serialVersionUID = 1L;

                                              @Override
                                              public String getAuthority() {
                                                return autority;
                                              }
                                            };
                                          })
                                      .collect(Collectors.toSet()));
                            }
                            throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Invalid username or password");
                          });
                } else {
                  return Mono.error(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST, "Invalid basic authentication"));
                }
              } else if (authType.equals(AuthType.BEARER)) {
                try {
                  DecodedJWT jwt = verifier.verify(JWT.decode(credentials));
                  return findByUsername(jwt.getSubject())
                      .map(
                          profile -> {
                            return new UsernamePasswordAuthenticationToken(
                                profile,
                                credentials,
                                profile.getAuthorities().stream()
                                    .map(ProfileAuthority::name)
                                    .map(
                                        autority -> {
                                          return new GrantedAuthority() {
                                            private static final long serialVersionUID = 1L;

                                            @Override
                                            public String getAuthority() {
                                              return autority;
                                            }
                                          };
                                        })
                                    .collect(Collectors.toSet()));
                          });
                } catch (JWTVerificationException ex) {
                  return Mono.error(
                      new ResponseStatusException(
                          HttpStatus.BAD_GATEWAY, "Invalid bearer authentication"));
                }
              } else {
                return Mono.error(
                    new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY, "Invalid type authentication"));
              }
            });
  }

  private Mono<Profile> findByUsername(String username) {
    return db.select()
        .from(Profile.class)
        .matching(ProfileCriteria.execute(ProfileFilter.builder().setUsername(username).build()))
        .fetch()
        .one()
        .switchIfEmpty(
            Mono.error(
                new UsernameNotFoundException(
                    "Profile with username " + username + " not found.")));
  }
}
