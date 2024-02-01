package ca.jrvs.apps.jdbc;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl("jdbc:postgresql://localhost:5432/lilPostgres");
    dataSource.setUsername("postgres");
    dataSource.setPassword("password");
    return dataSource;
  }
  @Bean
  public Connection connection(DataSource dataSource) throws SQLException {
    return dataSource.getConnection();
  }
}

