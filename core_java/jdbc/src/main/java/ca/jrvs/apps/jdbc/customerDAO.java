package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataAccessObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class customerDAO extends DataAccessObject<Customer>{
private static final String INSERT=
    "INSERT INTO customer (firstName, lastName, email, phone, address, city, state, zipCode) VALUES (?,?,?,?,?,?,?,?)";

  private static final String GET_ONE =
      "SELECT firstName, lastName, email, phone, address, city, state, zipCode, customer_id FROM customer WHERE customer_id=?";

public customerDAO(Connection connection) {
    super(connection);
  }

  private static final String UPDATE = "UPDATE customer SET firstName = ?, lastName=?, " +
      "email = ?, phone = ?, address = ?, city = ?, state = ?, zipCode = ? WHERE customer_id = ?";

  private static final String DELETE = "DELETE FROM customer WHERE customer_id = ?";

  @Override
  public Customer findById(long id) {
    Customer customer = new Customer();
    try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
      statement.setLong(1, id);
      ResultSet rs = statement.executeQuery();
      while(rs.next()){
        customer.setId(rs.getLong("customer_id"));
        customer.setFirstName(((ResultSet) rs).getString("FirstName"));
        customer.setLastName(rs.getString("LastName"));
        customer.setEmail(rs.getString("Email"));
        customer.setPhone(rs.getString("Phone"));
        customer.setAddress(rs.getString("Address"));
        customer.setCity(rs.getString("City"));
        customer.setState(rs.getString("State"));
        customer.setZipCode(rs.getString("Zipcode"));
      }
    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return customer;
//    return null;
  }

  @Override
  public List<Customer> findAll() {
    return null;
  }

  @Override
  public Customer update(Customer dto) {
    Customer customer = null;
    try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
      statement.setString(1, dto.getFirstName());
      statement.setString(2, dto.getLastName());
      statement.setString(3, dto.getEmail());
      statement.setString(4, dto.getPhone());
      statement.setString(5, dto.getAddress());
      statement.setString(6, dto.getCity());
      statement.setString(7, dto.getState());
      statement.setString(8, dto.getZipCode());
      statement.setLong(9, dto.getId());
      statement.execute();
      customer = this.findById(dto.getId());
    }catch(SQLException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return customer;
  }

  @Override
  public Customer create(Customer dto) {
    try(PreparedStatement statement = this.connection.prepareStatement(INSERT)){
      statement.setString(1, dto.getFirstName());
      statement.setString(2, dto.getLastName());
      statement.setString(3, dto.getEmail());
      statement.setString(4, dto.getPhone());
      statement.setString(5, dto.getAddress());
      statement.setString(6, dto.getCity());
      statement.setString(7, dto.getState());
      statement.setString(8, dto.getZipCode());
      statement.execute();
      int id = this.getLastVal(CUSTOMER_SEQUENCE);
      return this.findById(id);
     // return null;
    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }

  }
  @Override
  public void delete(long id) {
    try(PreparedStatement statement = this.connection.prepareStatement(DELETE);){
      statement.setLong(1, id);
      statement.execute();
    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}