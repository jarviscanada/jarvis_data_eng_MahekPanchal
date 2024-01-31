package ca.jrvs.apps.jdbc;


import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ca.jrvs.apps.jdbc.TestDataSourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(classes = TestDataSourceConfig.class)
public class QuoteDao_Test {
  private static final Logger logger = LoggerFactory.getLogger(QuoteDao_Test.class);
  @Autowired
  private DataSource dataSource;
  // private final QuoteDao quoteDao = new QuoteDao();

  private Connection connection;
  private QuoteDao quoteDao;
  private Object updateSql;

  @BeforeEach
  public void setup() throws SQLException {
    // Use the DataSource bean provided by TestDataSourceConfig
    connection = dataSource.getConnection();
    // Initialize QuoteDao with the test connection
    quoteDao = new QuoteDao(connection);

  }

  @Test
  public void testSaveAndFindById() throws SQLException {
    // Test data
    Date currentDate = new Date();
    Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
    Quote testQuote = new Quote("TEST", 10.0, 15.0, 8.0, 12.0, 100, currentDate, 11.0, 1.0, "1%", currentTimestamp);

    // Save the quote
    quoteDao.save(testQuote);

    // Find the saved quote by id
    Optional<Quote> retrievedQuote = quoteDao.findById("TEST");

    // Assertions
    assertTrue(retrievedQuote.isPresent());
    assertEquals(testQuote.getTicker(), retrievedQuote.get().getTicker());
    assertEquals(testQuote.getPrice(), retrievedQuote.get().getPrice());

  }


  @Test
  public void testFindAll() throws SQLException {
    // Save some test data
    Quote quote1 = new Quote("TEST1", 10.0, 15.0, 8.0, 12.0, 100, null, 11.0, 1.0, "1%", null);
    Quote quote2 = new Quote("TEST2", 20.0, 25.0, 18.0, 22.0, 200, null, 21.0, 2.0, "2%", null);
    quoteDao.save(quote1);
    quoteDao.save(quote2);

    // Add logging to print the number of records retrieved
    List<Quote> quotes = (List<Quote>) quoteDao.findAll();
    logger.info("Number of records retrieved: {}", quotes.size());

    // Add logging to print the details of each retrieved record
    quotes.forEach(quote -> logger.info("Retrieved Quote: {}", quote));

    // If you have issues with quotes being null, add additional logging
    if (quotes == null) {
      logger.error("Quotes list is null");
    }

    // Update the expected value to 3 since you have three rows in the database
    assertEquals(5, quotes.size(), "Number of quotes does not match the expected value");
  }


  // Assuming the timestamp format is "yyyy-MM-dd HH:mm:ss"
//  String timestampFormat = "yyyy-MM-dd HH:mm:ss";
//  SimpleDateFormat dateFormat = new SimpleDateFormat(timestampFormat);


  @Test
  public void testUpdate() throws SQLException {
    // Save a test quote

    Date currentDate = new Date();
    Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
    Quote originalQuote = new Quote("TEST2", 10.0, 15.0, 8.0, 12.0, 100, currentDate, 11.0, 1.0, "1%", currentTimestamp);

    // Save the quote
    quoteDao.save(originalQuote);

    // Log the data before the update
    logger.info("Before update: {}", originalQuote);

    try {
      // Log the actual update SQL query
      logger.info("Executing update SQL for symbol {}: {}", originalQuote.getTicker(), updateSql);

      // Delete the test quote from the database
      //quoteDao.deleteById(originalQuote.getTicker());

      // Update the test quote
      double updatedPrice = 12.0;
      originalQuote.setPrice(updatedPrice);

      // Convert the string representation of the date to a Date object
      String updatedLatestTradingDayString = "2024-01-30";
      Date updatedLatestTradingDay = new SimpleDateFormat("yyyy-MM-dd").parse(updatedLatestTradingDayString);

      // Set the updatedLatestTradingDay in the quote
      originalQuote.setLatestTradingDay(currentDate);
      // Log the updated quote before saving
      logger.info("Updated quote before save: {}", originalQuote);


      // Save the updated quote
      quoteDao.save(originalQuote);

      // Log the data after the update
      logger.info("After update: {}", originalQuote);

      // Retrieve the updated quote
      Optional<Quote> updatedQuote = quoteDao.findById("TEST2");
      logger.info("Retrieved quote: {}", updatedQuote.orElse(null));

      // Assertions
      assertTrue(updatedQuote.isPresent(), "Updated quote should be present");
      assertEquals(updatedPrice, updatedQuote.get().getPrice(), "Price should be updated");

    } catch (Exception e) {
      // Log any exceptions that might occur during the update
      logger.error("Error during update for symbol {}: {}", originalQuote.getTicker(), e.getMessage(), e);
      fail("Update operation failed: " + e.getMessage());
    }
  }
  
  @Test
  public void testDelete() throws SQLException {
    // Save a test quote
    Quote testQuote = new Quote("DELETE_TEST", 10.0, 15.0, 8.0, 12.0, 100, null, 11.0, 1.0, "1%", null);
    quoteDao.save(testQuote);

    // Delete the test quote
    quoteDao.deleteById("DELETE_TEST");

    // Attempt to find the deleted quote
    Optional<Quote> deletedQuote = quoteDao.findById("DELETE_TEST");

    // Assertions
    assertFalse(deletedQuote.isPresent());

  }
}



//package ca.jrvs.apps.jdbc;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//class QuoteDao {
//
//  private final Connection connection;
//
//  public QuoteDao(Connection connection) {
//    this.connection = connection;
//  }
//
//  public Optional<Quote> findById(String ticker) throws SQLException {
//    String sql = "SELECT * FROM quotes WHERE ticker = ?";
//    try (PreparedStatement statement = connection.prepareStatement(sql)) {
//      statement.setString(1, ticker);
//      ResultSet resultSet = statement.executeQuery();
//      if (resultSet.next()) {
//        Quote quote = new Quote();
//        quote.setTicker(resultSet.getString("ticker"));
//        quote.setPrice(resultSet.getDouble("last_price"));
//
//        return Optional.of(quote);
//      } else {
//        return Optional.empty();
//      }
//    }
//  }
//
//  public List<Quote> findAll() throws SQLException {
//    List<Quote> quotes = new ArrayList<>();
//    String sql = "SELECT * FROM quotes";
//    try (Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(sql)) {
//      while (resultSet.next()) {
//        Quote quote = new Quote();
//        quote.setTicker(resultSet.getString("ticker"));
//        quote.setPrice(resultSet.getDouble("last_price"));
//
//        quotes.add(quote);
//      }
//    }
//    return quotes;
//  }
//
//  public void update(Quote quote) throws SQLException {
//    String sql = "UPDATE quotes SET last_price = ? WHERE ticker = ?";
//    try (PreparedStatement statement = connection.prepareStatement(sql)) {
//      statement.setDouble(1, quote.getPrice());
//      statement.setString(2, quote.getTicker());
//      statement.executeUpdate();
//    }
//  }
//
//  public void delete(Quote quote) throws SQLException {
//    String sql = "DELETE FROM quotes WHERE ticker = ?";
//    try (PreparedStatement statement = connection.prepareStatement(sql)) {
//      statement.setString(1, quote.getTicker());
//      statement.executeUpdate();
//    }
//  }
//
//
//}
