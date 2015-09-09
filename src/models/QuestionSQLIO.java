/*
 * Start server: /usr/local/mysql/bin/mysqld
 * Connect: /usr/local/mysql/bin/mysql -u ... -p
 * File Overview: TODO
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author author
 */
public class QuestionSQLIO {
  
  static final String DB_NAME = "database";
  static final String TABLE_NAME = "question";
  static final String LOAD_DATABASE_QUERY = "SELECT * FROM " + DB_NAME + "." + TABLE_NAME;
  
  public static Connection getConnection() throws SQLException {

    Connection conn = DriverManager.getConnection(
      "jdbc:mysql://localhost/flashcards", "sambryant", "");
    return conn;
  }
  
  
  public static void doStuff() throws SQLException {
    Connection con = getConnection();
    Statement statement = con.createStatement();
    ResultSet rs = statement.executeQuery(LOAD_DATABASE_QUERY);
    
  }
  
  public static void main(String[] args) {
    
  }
  
}
