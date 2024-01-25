package ca.jrvs.apps.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class QuoteDao {

  private final Connection connection;

  public QuoteDao(Connection connection) {
    this.connection = connection;
  }

  public Optional<Quote> findById(String ticker) throws SQLException {
    String sql = "SELECT * FROM quotes WHERE ticker = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, ticker);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        Quote quote = new Quote();
        quote.setTicker(resultSet.getString("ticker"));
        quote.setPrice(resultSet.getDouble("last_price"));

        return Optional.of(quote);
      } else {
        return Optional.empty();
      }
    }
  }

  public List<Quote> findAll() throws SQLException {
    List<Quote> quotes = new ArrayList<>();
    String sql = "SELECT * FROM quotes";
    try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)) {
      while (resultSet.next()) {
        Quote quote = new Quote();
        quote.setTicker(resultSet.getString("ticker"));
        quote.setPrice(resultSet.getDouble("last_price"));

        quotes.add(quote);
      }
    }
    return quotes;
  }

  public void update(Quote quote) throws SQLException {
    String sql = "UPDATE quotes SET last_price = ? WHERE ticker = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setDouble(1, quote.getPrice());
      statement.setString(2, quote.getTicker());
      statement.executeUpdate();
    }
  }

  public void delete(Quote quote) throws SQLException {
    String sql = "DELETE FROM quotes WHERE ticker = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, quote.getTicker());
      statement.executeUpdate();
    }
  }


}
