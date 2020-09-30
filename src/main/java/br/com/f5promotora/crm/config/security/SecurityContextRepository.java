package br.com.f5promotora.crm.config.security;

import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

  private final AuthenticationManager authenticationManager;

  @Override
  public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange exchange) {
    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    if (authHeader != null) {
      StringTokenizer st = new StringTokenizer(authHeader);

      if (st.countTokens() == 2) {
        String authType = st.nextToken();

        if (authType.equalsIgnoreCase(AuthType.BEARER.name())) {
          return authenticationManager
              .authenticate(
                  new UsernamePasswordAuthenticationToken(AuthType.BEARER.name(), st.nextToken()))
              .map(authentication -> new SecurityContextImpl(authentication));
        } else if (authType.equalsIgnoreCase(AuthType.BASIC.name())) {
          return authenticationManager
              .authenticate(
                  new UsernamePasswordAuthenticationToken(AuthType.BASIC.name(), st.nextToken()))
              .map(authentication -> new SecurityContextImpl(authentication));
        } else {
          return authenticationManager
              .authenticate(
                  new UsernamePasswordAuthenticationToken(
                      AuthType.UNDEFINED.name(), st.nextToken()))
              .map(authentication -> new SecurityContextImpl(authentication));
        }
      } else {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Provide the token along with the authentication type");
      }
    }

    return Mono.empty();
  }
}
