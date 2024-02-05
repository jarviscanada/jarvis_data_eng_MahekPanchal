package ca.jrvs.apps.jdbc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

  public static void main(String[] args) {

    Map<String, String> properties = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/properties.txt"))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] tokens = line.split(":");
        properties.put(tokens[0], tokens[1]);
      }
    } catch (FileNotFoundException e) {
      logger.error("File not found", e);
    } catch (IOException e) {
      logger.error("Error reading file", e);
    }

    // Load JDBC driver
    try {
      Class.forName(properties.get("db-class"));
    } catch (ClassNotFoundException e) {
      logger.error("JDBC driver not found", e);
    }

    // Create database connection
    String url = "jdbc:postgresql://" + properties.get("server") + ":" + properties.get("port") + "/" + properties.get("database");
    try (Connection c = DriverManager.getConnection(url, properties.get("username"), properties.get("password"))) {
      // Instantiate DAOs (Data Access Objects)
      QuoteDao qRepo = new QuoteDao(c);
      PositionDao pRepo = new PositionDao(c);

      // Instantiate HTTP client and helper for Alpha Vantage API
     // OkHttpClient client = new OkHttpClient(); // declared in quotehttphelper
      QuoteHttpHelper rcon = new QuoteHttpHelper(properties.get("api-key"));

      // Instantiate services, injecting DAOs and API helper
      QuoteService sQuote = new QuoteService(qRepo, rcon);

      PositionService sPos = new PositionService(pRepo,sQuote);

      // Instantiate the main controller, injecting services
      StockQuoteController con = new StockQuoteController(sQuote, sPos);

      // Start the application
      con.initClient();

      SchedulerConfig sced = new SchedulerConfig(rcon,qRepo);
      // Call the scheduler to fetch and persist data
      sced.fetchDataAndPersist();
    } catch (SQLException e) {
      logger.error("Error establishing database connection", e);
    }
  }
}

