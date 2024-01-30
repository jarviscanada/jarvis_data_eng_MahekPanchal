package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class QuoteDao implements CrudDao<Quote, String> {
  private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
  private Connection connection;

  public QuoteDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public Quote save(Quote entity) throws IllegalArgumentException {
    String sql = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, " +
        "previous_close, change, change_percent, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, entity.getTicker());
      statement.setDouble(2, entity.getOpen());
      statement.setDouble(3, entity.getHigh());
      statement.setDouble(4, entity.getLow());
      statement.setDouble(5, entity.getPrice());
      statement.setInt(6, entity.getVolume());

      // Check if latestTradingDay is not null before calling getTime()
      if (entity.getLatestTradingDay() != null) {
        statement.setDate(7, new java.sql.Date(entity.getLatestTradingDay().getTime()));
      } else {
        statement.setNull(7, Types.DATE);
      }

      statement.setDouble(8, entity.getPreviousClose());
      statement.setDouble(9, entity.getChange());
      statement.setString(10, entity.getChangePercent());
      statement.setTimestamp(11, entity.getTimestamp());

      // Execute the update
      statement.executeUpdate();
    } catch (SQLException e) {
      // Logging the exception
      logger.error("Error saving quote entity. Symbol: {}", entity.getTicker(), e);
    }
    return entity;
  }

  @Override
  public Quote update(Quote entity) throws IllegalArgumentException {
    String sql = "UPDATE quote SET open=?, high=?, low=?, price=?, volume=?, latest_trading_day=?, " +
        "previous_close=?, change=?, change_percent=?, timestamp=? WHERE symbol=?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setDouble(1, entity.getOpen());
      statement.setDouble(2, entity.getHigh());
      statement.setDouble(3, entity.getLow());
      statement.setDouble(4, entity.getPrice());
      statement.setInt(5, entity.getVolume());

      // Check if latestTradingDay is not null before calling getTime()
      if (entity.getLatestTradingDay() != null) {
        statement.setDate(6, new java.sql.Date(entity.getLatestTradingDay().getTime()));
      } else {
        statement.setNull(6, Types.DATE);
      }

      statement.setDouble(7, entity.getPreviousClose());
      statement.setDouble(8, entity.getChange());
      statement.setString(9, entity.getChangePercent());
      statement.setTimestamp(10, entity.getTimestamp());
      statement.setString(11, entity.getTicker());

      // Execute the update
      statement.executeUpdate();
    } catch (SQLException e) {
      // Logging the exception
      logger.error("Error updating quote entity. Symbol: {}", entity.getTicker(), e);
    }
    return entity;
  }




  @Override
  public Optional<Quote> findById(String symbol) throws IllegalArgumentException {
    String sql = "SELECT * FROM quote WHERE symbol = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, symbol);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        // Map the ResultSet to a Quote object
        return Optional.of(mapToQuote(resultSet));
      }
    } catch (SQLException e) {
      logger.error("Error finding the quote by id. Symbol: {}", symbol, e);
    }
    return Optional.empty();
  }

  //  @Override
//  public Iterable<Quote> findAll() {
//    List<Quote> quotes = new ArrayList<>();
//    String sql = "SELECT * FROM quote";
//    try (Statement statement = connection.createStatement()) {
//      ResultSet resultSet = statement.executeQuery(sql);
//      while (resultSet.next()) {
//        quotes.add(mapToQuote(resultSet));
//      }
//    } catch (SQLException e) {
//      logger.error("Error finding all quotes", e);
//    }
//    return quotes;
//  }
  @Override
  public Iterable<Quote> findAll() {
    List<Quote> quotes = new ArrayList<>();
    String sql = "SELECT * FROM quote";
    // Add this logging statement before executing the query
    logger.info("Executing SQL query: {}", sql);

    try (Statement statement = connection.createStatement()) {
      logger.info("Database connection established successfully.");
      ResultSet resultSet = statement.executeQuery(sql);
      while (resultSet.next()) {
        Quote quote = mapToQuote(resultSet);
        quotes.add(quote);
        logger.info("Retrieved Quote: {}", quote);
      }
    } catch (SQLException e) {
      logger.error("Error establishing database connection", e);
      logger.error("Error finding all quotes", e);
    }
    return quotes;
  }

  @Override
  public void deleteById(String symbol) throws IllegalArgumentException {
    String sql = "DELETE FROM quote WHERE symbol = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, symbol);
      statement.executeUpdate();
    } catch (SQLException e) {
      logger.error("Error deleting quote by id. Symbol: {}", symbol, e);
    }
  }

  @Override
  public void deleteAll() {
    String sql = "DELETE FROM quote";
    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      logger.error("Error deleting all quotes", e);
      throw new RuntimeException("Error deleting all quotes", e);
    }
  }

  private Quote mapToQuote(ResultSet resultSet) throws SQLException {
    try{
      String ticker = resultSet.getString("symbol");
      double open = resultSet.getDouble("open");
      double high = resultSet.getDouble("high");
      double low = resultSet.getDouble("low");
      double price = resultSet.getDouble("price");
      int volume = resultSet.getInt("volume");
      java.sql.Date latestTradingDay = resultSet.getDate("latest_trading_day");
      double previousClose = resultSet.getDouble("previous_close");
      double change = resultSet.getDouble("change");
      String changePercent = resultSet.getString("change_percent");
      Timestamp timestamp = resultSet.getTimestamp("timestamp");

      return new Quote(ticker, open, high, low, price, volume, latestTradingDay, previousClose,
          change, changePercent, timestamp);
    } catch (SQLException e) {
      logger.error("Error mapping ResultSet to Quote", e);
      throw e;
    }
  }
}






