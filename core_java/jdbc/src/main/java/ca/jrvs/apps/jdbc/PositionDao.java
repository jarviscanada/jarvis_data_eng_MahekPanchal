package ca.jrvs.apps.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PositionDao implements CrudDao<Position, String> {
  private static final Logger logger = LoggerFactory.getLogger(PositionDao.class);
  private final Connection connection;

  //@Autowired
  public PositionDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public Position save(Position entity) throws IllegalArgumentException {
    String sql = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, entity.getTicker());
      statement.setInt(2, entity.getNumOfShares());
      statement.setDouble(3, entity.getValuePaid());
      statement.executeUpdate();
    } catch (SQLException e) {
      logger.error("Error saving position entity. Ticker: {}", entity.getTicker(), e);
    }
    return entity;
  }

  @Override
  public Optional<Position> findById(String ticker) throws IllegalArgumentException {
    String sql = "SELECT * FROM position WHERE symbol = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, ticker);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return Optional.of(mapResultSetToPosition(resultSet));
      }
    } catch (SQLException e) {
      logger.error("Error finding position by id. Ticker: {}", ticker, e);
    }
    return Optional.empty();
  }


  public List<Position> findAll() throws SQLException {
    try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM position")) {

      List<Position> positions = new ArrayList<>();
      while (resultSet.next()) {
        Position position = mapResultSetToPosition(resultSet);
        positions.add(position);
      }
      return positions;
    }
  }

  @Override
  public void deleteById(String ticker) throws IllegalArgumentException {
    String sql = "DELETE FROM position WHERE symbol = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, ticker);
      statement.executeUpdate();
    } catch (SQLException e) {
      logger.error("Error deleting position by ticker. Ticker: {}", ticker, e);
    }
  }

  @Override
  public void deleteAll() {
    String sql = "DELETE FROM position";
    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      logger.error("Error deleting all positions", e);
    }
  }

  public void delete(Position position) throws SQLException {
    String sql = "DELETE FROM position WHERE symbol = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, position.getTicker());
      statement.executeUpdate();
    } catch (SQLException e) {
      logger.error("Error deleting position by ticker. Ticker: {}", position.getTicker(), e);
    }
  }

  @Override
  public void update(Position entity) throws IllegalArgumentException {
    String sql = "UPDATE position SET number_of_shares = ?, value_paid = ? WHERE symbol = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, entity.getNumOfShares());
      statement.setDouble(2, entity.getValuePaid());  //UPDATED THIS
      statement.setString(3, entity.getTicker());
      statement.executeUpdate();
    } catch (SQLException e) {
      logger.error("Error updating position. Ticker: {}", entity.getTicker(), e);
    }
  }


  private Position mapResultSetToPosition(ResultSet resultSet) throws SQLException {
    String ticker = resultSet.getString("symbol");
    int numOfShares = resultSet.getInt("number_of_shares");
    double valuePaid = resultSet.getDouble("value_paid");
    return new Position(ticker, numOfShares, valuePaid);
  }


}

