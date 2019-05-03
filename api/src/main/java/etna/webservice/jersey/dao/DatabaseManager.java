package etna.webservice.jersey.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
	private Connection connection;
	
	public DatabaseManager( ) {
		connect();
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public void connect() {
	      try {
	         Class.forName("org.postgresql.Driver");
	      }
	      catch (ClassNotFoundException e) {
	         System.err.println (e);
	         System.exit (-1);
	      }
	      try {
	    	  this.connection = DriverManager.getConnection(
	         "jdbc:postgresql://127.0.0.1:5432/RestauAdvisor", "postgres", "root");
	      }
	      catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	      }
	   }
	
	public void disconnect( ) {
		if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
