package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCexecutor {

  public static void main(String[] args) {
    DatabaseConnectionManager dbcon = new DatabaseConnectionManager("localhost",
        "lilPostgres","postgres", "password");
    try{
      Connection connection = dbcon.getConnection();
      customerDAO CusDAO = new customerDAO(connection);
      Customer customer = new Customer();
      customer.setFirstName("Tom");
      customer.setLastName("Pot");
      customer.setEmail("tom@gmail.com");
      customer.setPhone("7778764950");
      customer.setAddress("65 East St");
      customer.setCity("Calgary");
      customer.setState("Alberta");
      customer.setZipCode("N2V6M4");

      Customer dbCustomer= CusDAO.create(customer);
      System.out.println(dbCustomer);
      dbCustomer= CusDAO.findById(dbCustomer.getId());
      System.out.println(dbCustomer);
      dbCustomer.setEmail("mahek@deletion.com");
      dbCustomer = CusDAO.update(dbCustomer);
      System.out.println(dbCustomer);
      CusDAO.delete(dbCustomer.getId());



//      Customer customer = CusDAO.findById(3);
//      System.out.println(customer.getFirstName() + " " + customer.getLastName() + " " + customer.getEmail());
//      customer.setEmail("mahek@gmail.com");
//      CusDAO.update(customer);
//      System.out.println(customer.getFirstName() + " " + customer.getLastName());
//      Customer customer = new Customer();
//      customer.setFirstName("Mahek");
//      customer.setLastName("Panchal");
//      customer.setEmail("abc@gmail.com");
//      customer.setPhone("7778764950");
//      customer.setAddress("56 North St");
//      customer.setCity("Toronto");
//      customer.setState("Ontario");
//      customer.setZipCode("N2J3B8");
//      CusDAO.create(customer);

    }catch (SQLException e){
      e.printStackTrace();
    }
  }

}
