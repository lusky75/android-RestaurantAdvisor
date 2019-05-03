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

public class RestaurantDAO {
	
	public ArrayList<Restaurant> getAll() {
		Restaurant restaurant = null;
		MenuDAO menudao = new MenuDAO();
		ArrayList<Restaurant> list = new ArrayList<Restaurant>();
		DatabaseManager database = new DatabaseManager();
		String query = "SELECT * FROM public.\"Restaurant\"";
	    try {
	    	Statement statement = database.getConnection().createStatement ();
	    	ResultSet rs = statement.executeQuery (query);
	    	
	    	if (!rs.isBeforeFirst())
	    		return null;
	    	while ( rs.next() ) {
	    		restaurant = new Restaurant();
	    		restaurant.setId(rs.getLong("id"));
	    		restaurant.setName(rs.getString("name"));
	    		restaurant.setDescription(rs.getString("description"));
	    		restaurant.setLocation(rs.getString("location"));
	    		restaurant.setPhone(rs.getString("phone"));
	    		restaurant.setWebsite(rs.getString("website"));
	    		restaurant.setWeekHour(rs.getString("weekHour"));
	    		restaurant.setWeekEndHour(rs.getString("weekEndHour"));
	    		restaurant.setRating(rs.getInt("rating"));
	    		restaurant.setMenu(menudao.getByRestaurantId(restaurant.getId()));
	    		list.add(restaurant);
	    	}
	        database.disconnect();
	    } catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	    }
	    return (list);
	}
	
	public Restaurant getById(long id) {
		Restaurant restaurant = new Restaurant();
		DatabaseManager database = new DatabaseManager();
		MenuDAO menudao = new MenuDAO();
		String query = "SELECT * FROM public.\"Restaurant\" WHERE id = ?";
	    try {
	    	
	    	PreparedStatement statement = database.getConnection().prepareStatement (query);
	    	statement.setLong(1, id);
	    	ResultSet rs = statement.executeQuery ();
	    	if (!rs.next())
	    		return null;
	    	restaurant.setId(id);
			restaurant.setName(rs.getString("name"));
			restaurant.setDescription(rs.getString("description"));
			restaurant.setLocation(rs.getString("location"));
			restaurant.setPhone(rs.getString("phone"));
			restaurant.setWebsite(rs.getString("website"));
			restaurant.setWeekHour(rs.getString("weekHour"));
			restaurant.setWeekEndHour(rs.getString("weekEndHour"));
			restaurant.setMenu(menudao.getByRestaurantId(restaurant.getId()));
			restaurant.setRating(rs.getInt("rating"));
	        database.disconnect();
	    } catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	    }
	    return (restaurant);
	}
	
	public ArrayList<Restaurant> getByName(String name) {
		Restaurant restaurant = null;
		MenuDAO menudao = new MenuDAO();
		ArrayList<Restaurant> list = new ArrayList<Restaurant>();
		DatabaseManager database = new DatabaseManager();
		String query = "SELECT * FROM public.\"Restaurant\" WHERE name LIKE ?";
	    try {
	    	
	    	PreparedStatement statement = database.getConnection().prepareStatement (query);
	    	statement.setString(1, "%" + name + "%");
	    	ResultSet rs = statement.executeQuery ();

	    	if (!rs.isBeforeFirst())
	    		return null;
	    	while ( rs.next() ) {
	    		restaurant = new Restaurant();
	    		restaurant.setId(rs.getLong("id"));
	    		restaurant.setName(rs.getString("name"));
	    		restaurant.setDescription(rs.getString("description"));
	    		restaurant.setLocation(rs.getString("location"));
	    		restaurant.setPhone(rs.getString("phone"));
	    		restaurant.setWebsite(rs.getString("website"));
	    		restaurant.setWeekHour(rs.getString("weekHour"));
	    		restaurant.setWeekEndHour(rs.getString("weekEndHour"));
	    		restaurant.setMenu(menudao.getByRestaurantId(restaurant.getId()));
	    		restaurant.setRating(rs.getInt("rating"));
	    		list.add(restaurant);
	    	}
	        database.disconnect();
	    } catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	    }
	    return (list);
	}
	
	public boolean insertRestaurant(Restaurant restau) {
		String sql = "INSERT INTO public.\"Restaurant\" (name, description, location, phone, website, rating, weekhour, weekendhour) "
    			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		DatabaseManager database = new DatabaseManager();
		boolean answer = false;
		try {
	        PreparedStatement statement = database.getConnection().prepareStatement(sql);
	        statement.setString(1, restau.getName());
	        statement.setString(2, restau.getDescription());
	        statement.setString(3, restau.getLocation());
	        statement.setString(4, restau.getPhone());
	        statement.setString(5, restau.getWebsite());
	        statement.setInt(6, restau.getRating());
	        statement.setString(7, restau.getWeekHour());
	        statement.setString(8, restau.getWeekEndHour());
	        if(statement.executeUpdate() == 1)
	        	answer = true;
	        database.disconnect();
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}
	    return answer;   
	}
	
	public boolean updateRestaurant(Restaurant restau) {
		String query = makeQuery(restau);
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
	
	public String makeQuery(Restaurant restaurant) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String query = "UPDATE public.\"Restaurant\" SET ";
		if (restaurant.getName() != null)
			map.put("name", restaurant.getName());
		if (restaurant.getDescription() != null)
			map.put("description", restaurant.getDescription());
		if (restaurant.getLocation() != null)
			map.put("location", restaurant.getLocation());
		if (restaurant.getPhone() != null)
			map.put("phone", restaurant.getPhone());
		if (restaurant.getWebsite() != null)
			map.put("website", restaurant.getWebsite());
		if (restaurant.getWeekHour() != null)
			map.put("weekhour", restaurant.getWeekHour());
		if (restaurant.getWeekEndHour() != null)
			map.put("weekendhour", restaurant.getWeekEndHour());
		Iterator<Entry<String, Object>> it =  map.entrySet().iterator();
		if (!it.hasNext())
			return null;
		while (it.hasNext()) {
			Entry<String, Object> pair = it.next();
			query += pair.getKey() + "='" + pair.getValue()+ "'";
			if (it.hasNext())
				query += ", ";
		}
		query += " WHERE id=" + restaurant.getId() + ";";
		return query;
	}
	
	public boolean deleteRestaurant(Restaurant restau) {
		String sql = "DELETE FROM public.\"Restaurant\" WHERE id = ?";
		DatabaseManager database = new DatabaseManager();
		boolean answer = false;
		try {
	        PreparedStatement statement = database.getConnection().prepareStatement(sql);
	        statement.setLong(1, restau.getId());
	        if(statement.executeUpdate() == 1)
	        	answer = true;
	        database.disconnect();
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}
	    return answer;   
	}
}