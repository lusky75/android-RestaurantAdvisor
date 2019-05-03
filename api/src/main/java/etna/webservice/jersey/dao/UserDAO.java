package etna.webservice.jersey.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import etna.webservice.jersey.model.Restaurant;
import etna.webservice.jersey.model.User;

public class UserDAO {
	
	public boolean userExists(User user) {
		String query = "SELECT * FROM public.\"users\" WHERE username=" + "'"+user.getUsername()+"'";
		DatabaseManager db = new DatabaseManager();
		try {
			Statement statement = db.getConnection().createStatement();
			ResultSet rs = statement.executeQuery (query);
			db.disconnect();
			if (rs.next()) {
				user.setId(rs.getLong("id"));
				return true;
			}
		} catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	      }
		return false;
	}
	
	public ArrayList<User> getAll() {
		User user = null;
		ArrayList<User> list = new ArrayList<User>();
		DatabaseManager database = new DatabaseManager();
		String query = "SELECT * FROM public.\"users\"";
	    try {
	    	Statement statement = database.getConnection().createStatement ();
	    	ResultSet rs = statement.executeQuery (query);

	    	if (!rs.isBeforeFirst())
	    		return null;
	    	while ( rs.next() ) {
	    		user = new User();
	    		user.setId(rs.getLong("id"));
	    		user.setUsername(rs.getString("username"));
				user.setPassword("hidden");
				user.setAdmin(rs.getBoolean("admin"));
				user.setEmail(rs.getString("email"));
				user.setCity(rs.getString("city"));
				user.setCountry(rs.getString("country"));
				user.setFirstname(rs.getString("firstname"));
				user.setLastname(rs.getString("lastname"));
				user.setBirthday(rs.getString("birthday"));
	    		list.add(user);
	    	}
	        database.disconnect();
	    } catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	    }
	    return (list);
	}
	
	public User getById(long id) {
		User user = new User();
		DatabaseManager database = new DatabaseManager();
		String query = "SELECT * FROM public.\"users\" WHERE id=?";
		try {

			PreparedStatement statement = database.getConnection().prepareStatement(query);
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();

			if (!rs.next())
				return null;
			user.setId(rs.getLong("id"));
			user.setUsername(rs.getString("username"));
			user.setPassword("hidden");
			user.setAdmin(rs.getBoolean("admin"));
			user.setEmail(rs.getString("email"));
			user.setCity(rs.getString("city"));
			user.setCountry(rs.getString("country"));
			user.setFirstname(rs.getString("firstname"));
			user.setLastname(rs.getString("lastname"));
			user.setBirthday(rs.getString("birthday"));
			database.disconnect();
		} catch (java.sql.SQLException e) {
			System.err.println(e);
			System.exit(-1);
		}
		return user;
	}
	
	public User getByUsername(String username) {
		User user = new User();
		DatabaseManager database = new DatabaseManager();
		String query = "SELECT * FROM public.\"users\" WHERE username=?";
		try {

			PreparedStatement statement = database.getConnection().prepareStatement(query);
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();

			if (!rs.next())
				return null;
			user.setId(rs.getLong("id"));
			user.setUsername(rs.getString("username"));
			user.setPassword("hidden");
			user.setAdmin(rs.getBoolean("admin"));
			user.setEmail(rs.getString("email"));
			user.setCity(rs.getString("city"));
			user.setCountry(rs.getString("country"));
			user.setFirstname(rs.getString("firstname"));
			user.setLastname(rs.getString("lastname"));
			user.setBirthday(rs.getString("birthday"));
			database.disconnect();
		} catch (java.sql.SQLException e) {
			System.err.println(e);
			System.exit(-1);
		}
		return user;
	}
	
	public boolean insertUser(User user) {
		String query = "INSERT INTO public.\"users\"( username, password) values (?, ?);";
		DatabaseManager db = new DatabaseManager();
		boolean answer = false;
		try {
			PreparedStatement statement = db.getConnection().prepareStatement(query);
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			answer = statement.execute();
			db.disconnect();
			
		} catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	      }
		userExists(user);
		return answer;
	}
	
	public boolean authUser(User user) {
		String query = "SELECT * FROM public.\"users\" WHERE username=? AND password=?";
		DatabaseManager db = new DatabaseManager();
		try {
			PreparedStatement statement = db.getConnection().prepareStatement(query);
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			ResultSet rs = statement.executeQuery();
			if (!rs.next()) 
				return false;
			user.setAdmin(rs.getBoolean("admin"));
			user.setFirstname(rs.getString("firstname"));
			user.setLastname(rs.getString("lastname"));
			db.disconnect();
		}
		catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	      }
		return true;
	}
	
	public boolean deleteUser(User user) {
		String sql = "DELETE FROM public.\"users\" WHERE id = ?";
		DatabaseManager database = new DatabaseManager();
		boolean answer = false;
		try {
	        PreparedStatement statement = database.getConnection().prepareStatement(sql);
	        statement.setLong(1, user.getId());
	        if(statement.executeUpdate() == 1)
	        	answer = true;
	        database.disconnect();
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}
	    return answer;   
	}
	
	public boolean updateUser(User user) {
		String query = makeQuery(user);
		if (query == null)
			return false;
		boolean answer = false;
		DatabaseManager database = new DatabaseManager();
		Statement statement;
		try {
			statement = database.getConnection().createStatement ();
			if (statement.executeUpdate(query) == 1)
				answer = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return answer;
	}
	
	public String makeQuery(User user) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String query = "UPDATE public.\"users\" SET ";
		if (user.getFirstname() != null)
			map.put("firstname", user.getFirstname());
		if (user.getLastname() != null)
			map.put("lastname", user.getLastname());
		if (user.getEmail() != null)
			map.put("email", user.getEmail());
		if (user.getCity() != null)
			map.put("city", user.getCity());
		if (user.getCountry() != null)
			map.put("country", user.getCountry());
		if (user.getBirthday() != null)
			map.put("birthday", user.getBirthday());
		Iterator<Entry<String, Object>> it =  map.entrySet().iterator();
		if (!it.hasNext())
			return null;
		while (it.hasNext()) {
			Entry<String, Object> pair = it.next();
			query += pair.getKey() + "='" + pair.getValue()+ "'";
			if (it.hasNext())
				query += ", ";
		}
		query += " WHERE id=" + user.getId() + ";";
		return query;
	}
}
