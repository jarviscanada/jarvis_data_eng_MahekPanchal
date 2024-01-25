//package ca.jrvs.apps.jdbc;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class PositionDaoTest {
//
//  private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/test_db";
//  private static final String DB_USER = "your_db_user";
//  private static final String DB_PASSWORD = "your_db_password";
//
//  private Connection connection;
//  private PositionDao positionDao;
//
//  @BeforeEach
//  void setUp() throws SQLException {
//    connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
//    positionDao = new PositionDao(connection);
//  }
//
//  @Test
//  void testFindById() throws SQLException {
//    // Assuming you have a valid positionId in your test database
//    Optional<Position> position = positionDao.findById(1);
//    assertNotNull(position);
//    assertTrue(position.isPresent());
//    assertEquals(1, position.get().getPositionId());
//  }
//
//  @Test
//  void testFindAll() throws SQLException {
//    List<Position> positions = positionDao.findAll();
//    assertNotNull(positions);
//    assertTrue(positions.size() > 0);
//  }
//
//  @Test
//  void testUpdate() throws SQLException {
//    // Assuming you have a valid positionId in your test database
//    Optional<Position> existingPosition = positionDao.findById(1);
//    assertTrue(existingPosition.isPresent());
//
//    Position updatedPosition = existingPosition.get();
//    updatedPosition.setQuantity(100); // Update the quantity
//
//    positionDao.update(updatedPosition);
//
//    // Retrieve the position again and assert that the quantity has been updated
//    Optional<Position> retrievedPosition = positionDao.findById(1);
//    assertTrue(retrievedPosition.isPresent());
//    assertEquals(100, retrievedPosition.get().getQuantity());
//  }
//
//  @Test
//  void testDelete() throws SQLException {
//    // Assuming you have a valid positionId in your test database
//    Position position = new Position();
//    position.setPositionId(1);
//    positionDao.delete(position);
//    // Try to retrieve the position again, it should be null
//    Optional<Position> deletedPosition = positionDao.findById(1);
//    assertFalse(deletedPosition.isPresent());
//  }
//}
