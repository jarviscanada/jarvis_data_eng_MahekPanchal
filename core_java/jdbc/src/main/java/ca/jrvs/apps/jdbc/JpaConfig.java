package ca.jrvs.apps.jdbc;

// JpaConfig.java
/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EntityManagerFactoryBuilder;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "ca.jrvs.apps.jdbc")
@EntityScan(basePackages = "ca.jrvs.apps.jdbc")
public class JpaConfig {

  @Autowired
  private DataSource dataSource;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(dataSource)
        .packages("ca.jrvs.apps.jdbc")
        .persistenceUnit("yourPersistenceUnitName")
        .build();
  }
}*/
