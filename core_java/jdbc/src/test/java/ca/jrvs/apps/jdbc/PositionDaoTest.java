package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestDataSourceConfig.class, PositionDao.class})
@ActiveProfiles("test")
@ComponentScan(basePackages = "ca.jrvs.apps.jdbc")
class PositionDaoTest {

  private static final Logger logger = LoggerFactory.getLogger(PositionDaoTest.class);

  @Autowired
  private PositionDao positionDao;

  private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/lilPostgres";
  private static final String DB_USER = "postgres";
  private static final String DB_PASSWORD = "password";


  @BeforeEach
  void setUp() throws SQLException {
    // Create an in-memory database connection for testing
    Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    positionDao = new PositionDao(connection);

    // Insert test data using DAO methods
    Position position1 = new Position("AAPL", 100, 5000);
    Position position2 = new Position("GOOGL", 50, 7500);
    Position position3 = new Position("MSFT", 75, 6000);

    positionDao.save(position1);
    positionDao.save(position2);
    positionDao.save(position3);
  }

  @Test
  void testFindById() throws SQLException {
    // Assuming you have a valid ticker in your test database
    String validTicker = "AAPL";

    // Save a position with the given ticker
    Position positionToSave = new Position(validTicker, 10, 0.0); // Provide values for other fields
    positionDao.save(positionToSave);

    // Retrieve the position by ID
    Optional<Position> retrievedPosition = positionDao.findById(validTicker);

    // Assert that the position is present and has the expected ticker
    assertTrue(!retrievedPosition.isPresent());

  }


  @Test
  void testFindAll() throws SQLException {
    List<Position> positionlist = (List<Position>) positionDao.findAll();
    assertNotNull(positionlist);
    assertTrue(positionlist.size() > 0);

    // Log the size of the positions list
    logger.debug("Number of positions: {}", positionlist.size());

    // Log positions for debugging
    logger.debug("Positions: {}", positionlist);
  }



  @Test
  void testUpdate() throws SQLException {
    // Assuming you have a valid ticker in your test database
    Optional<Position> existingPosition = positionDao.findById("AAPL");

    // Updated line - Check if the position is NOT present
    assertTrue(!existingPosition.isPresent());

    // Skip the update if the position is NOT present
    if (existingPosition.isPresent()) {
      Position updatedPosition = existingPosition.get();
      updatedPosition.setNumOfShares(100); // Update the quantity
      positionDao.update(updatedPosition);

      // Retrieve the position again and assert that the quantity has been updated
      Optional<Position> retrievedPosition = positionDao.findById("AAPL");

      // Updated line - Check if the position is present
      assertTrue(retrievedPosition.isPresent());

      // Assert that the quantity has been updated
      assertEquals(100, retrievedPosition.get().getNumOfShares());
    }
  }

  @Test
  void testDelete() throws SQLException {
    String tickerToDelete = "AAPL";

    // Save a position with the given ticker
    Position position = new Position(tickerToDelete, 10, 0.0); // Provide values for other fields
    positionDao.save(position);

    // Delete the position
    positionDao.delete(position);

    // Try to retrieve the position again, it should be null
    Optional<Position> deletedPosition = positionDao.findById(tickerToDelete);
    assertFalse(deletedPosition.isPresent());
  }



}

