package br.com.f5promotora.crm.config.database.r2dbc;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.ReactiveTransactionManager;

@Configuration
@EnableR2dbcRepositories("br.com.f5promotora.crm.resource.r2dbc.repository")
public class R2dbcConfig extends AbstractR2dbcConfiguration {

  @Value("${spring.r2dbc.url}")
  private String url;

  @Override
  public ConnectionFactory connectionFactory() {
    return ConnectionFactories.get(url);
  }

  @Override
  protected List<Object> getCustomConverters() {
    return List.of(new UUIDToByteArrayConverter(), new ByteArrayToUUIDConverter());
  }

  @Bean
  public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
    DefaultReactiveDataAccessStrategy strategy =
        new DefaultReactiveDataAccessStrategy(MySqlDialect.INSTANCE);
    DatabaseClient databaseClient =
        DatabaseClient.builder()
            .connectionFactory(connectionFactory)
            .dataAccessStrategy(strategy)
            .build();
    return new R2dbcEntityTemplate(databaseClient, strategy);
  }

  @Bean
  public ReactiveTransactionManager reactiveTransactionManager(
      ConnectionFactory connectionFactory) {
    return new R2dbcTransactionManager(connectionFactory);
  }
}
