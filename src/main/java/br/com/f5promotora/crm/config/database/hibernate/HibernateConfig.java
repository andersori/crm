package br.com.f5promotora.crm.config.database.hibernate;

import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("br.com.f5promotora.crm.resource.jpa.repository")
public class HibernateConfig {

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.username}")
  private String user;

  @Value("${spring.datasource.password}")
  private String pass;

  @Bean
  public DataSource dataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(user);
    dataSource.setPassword(pass);
    dataSource.setMaximumPoolSize(10);
    return dataSource;
  }

  @Bean
  public LocalSessionFactoryBean entityManagerFactory(HibernateProperties properties) {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());
    sessionFactory.setPhysicalNamingStrategy(PhysicalNamingStrategyStandardImpl.INSTANCE);
    sessionFactory.setPackagesToScan(
        new String[] {"br.com.f5promotora.crm.domain.data.entity.jpa"});
    sessionFactory.setHibernateProperties(
        new Properties() {
          private static final long serialVersionUID = 1L;

          {
            putAll(properties.getProperties());
          }
        });
    return sessionFactory;
  }

  @Bean
  public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
    HibernateTransactionManager txManager = new HibernateTransactionManager();
    txManager.setSessionFactory(sessionFactory);
    return txManager;
  }

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }
}
