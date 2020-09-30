package br.com.f5promotora.crm.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(
            new Components()
                .addSecuritySchemes(
                    "basic",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic")
                        .in(In.HEADER)
                        .name("Authorization"))
                .addSecuritySchemes(
                    "bearer-jwt",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .in(In.HEADER)
                        .name("Authorization")
                        .bearerFormat("JWT")))
        .info(apiInfo());
  }

  private Info apiInfo() {
    return new Info()
        .title("CRM")
        .version("1.0.0")
        .license(new License().name("Apache 2.0").url("http://springdoc.org"));
  }

  @Bean
  GroupedOpenApi streamAPI() {
    String[] paths = {"/stream/**"};
    String[] packagedToMatch = {"br.com.f5promotora.crm.api.stream"};
    return GroupedOpenApi.builder()
        .group("stream+json")
        .pathsToMatch(paths)
        .packagesToScan(packagedToMatch)
        .build();
  }

  @Bean
  GroupedOpenApi controllerAPI() {
    String[] paths = {"/stream/**"};
    String[] packagedToMatch = {"br.com.f5promotora.crm.api.controller"};
    return GroupedOpenApi.builder()
        .group("json")
        .pathsToExclude(paths)
        .packagesToScan(packagedToMatch)
        .build();
  }
}
