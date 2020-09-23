package br.com.f5promotora.crm.config.database.hibernate;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.jpa")
public class HibernateProperties {
  private Map<String, String> properties = new HashMap<>();
}
