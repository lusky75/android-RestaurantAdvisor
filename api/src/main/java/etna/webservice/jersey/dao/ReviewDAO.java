package etna.webservice.jersey.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import etna.webservice.jersey.model.Review;
import etna.webservice.jersey.model.User;

public class ReviewDAO {
	
	public boolean reviewExists(User user, Review review) {
		try {
			DatabaseManager database = new DatabaseManager();
			String sql = "SELECT * FROM public.\"Review\" WHERE restaurant_id=" + review.getRestaurant_id()
					+ "AND user_id=" + user.getId();
			Statement statement = database.getConnection().createStatement();
			ResultSet result = statement.executeQuery(sql);
			if (result.isBeforeFirst()) {
				review = getById(result.getLong("id"));
				database.disconnect();
				return true;
			}
		} catch (java.sql.SQLException e) {
			System.err.println(e);
			System.exit(-1);
		}
		 return false;
	}
	
	public Review getById(long id) {
		Review review = new Review();
		RestaurantDAO restaurantdao = new RestaurantDAO();
		UserDAO userdao = new UserDAO();
		DatabaseManager database = new DatabaseManager();
		String query = "SELECT * FROM public.\"Review\" WHERE id =" + id;
	    try {
	    	Statement statement = database.getConnection().createStatement();
	    	ResultSet rs = statement.executeQuery(query);
	    	if(!rs.next())
	    		return null;
	    	review.setId(id);
	    	review.setRestaurant_id(rs.getLong("restaurant_id"));
	    	review.setRestaurant(restaurantdao.getById(rs.getLong("restaurant_id")).getName());
	   		review.setMessage(rs.getString("message"));
	   		review.setRating(rs.getInt("rating"));
	   		review.setUser_id(rs.getLong("user_id"));
	   		review.setRestaurant(restaurantdao.getById(rs.getLong("restaurant_id")).getName());
	   		review.setUsername(userdao.getById(rs.getLong("user_id")).getUsername());
	        database.disconnect();
	    } catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	    }
	    return (review);
	}
	
	public ArrayList<Review> getByUserId(long id) {
		Review review = new Review();
		ArrayList<Review> list = new ArrayList<Review>();
		DatabaseManager database = new DatabaseManager();
		RestaurantDAO restaurantdao = new RestaurantDAO();
		UserDAO userdao = new UserDAO();
		String query = "SELECT * FROM public.\"Review\" WHERE user_id=" + id;
	    try {
	    	Statement statement = database.getConnection().createStatement();
	    	ResultSet rs = statement.executeQuery(query);
	    	while(rs.next()) {
	    		review = new Review();
	    		review.setId(rs.getLong("id"));
	    		review.setRestaurant_id(rs.getLong("restaurant_id"));
	    		review.setMessage(rs.getString("message"));
	    		review.setRating(rs.getInt("rating"));
	    		review.setUser_id(rs.getLong("user_id"));
	    		review.setRestaurant(restaurantdao.getById(rs.getLong("restaurant_id")).getName());
		   		review.setUsername(userdao.getById(rs.getLong("user_id")).getUsername());
	    		list.add(review);
	    	}
	    	database.disconnect();
	    } catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	    }
	    return (list);
	}
	
	public ArrayList<Review> getByRestaurantId(long id) {
		Review review = new Review();
		ArrayList<Review> list = new ArrayList<Review>();
		DatabaseManager database = new DatabaseManager();
		RestaurantDAO restaurantdao = new RestaurantDAO();
		UserDAO userdao = new UserDAO();
		String query = "SELECT * FROM public.\"Review\" WHERE restaurant_id =" + id;
	    try {
	    	Statement statement = database.getConnection().createStatement();
	    	ResultSet rs = statement.executeQuery(query);
	    	while(rs.next()) {
	    		review = new Review();
	    		review.setId(rs.getLong("id"));
	    		review.setRestaurant_id(rs.getLong("restaurant_id"));
	    		review.setMessage(rs.getString("message"));
	    		review.setRating(rs.getInt("rating"));
	    		review.setUser_id(rs.getLong("user_id"));
	    		review.setRestaurant(restaurantdao.getById(rs.getLong("restaurant_id")).getName());
		   		review.setUsername(userdao.getById(rs.getLong("user_id")).getUsername());
	    		list.add(review);
	    	}
	    	database.disconnect();
	    } catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	    }
	    return (list);
	}
	
	public int getAverage(long restaurant_id) {
	    try {
	    	DatabaseManager database = new DatabaseManager();
	    	String sql = "SELECT AVG(rating) as average FROM public.\"Review\" WHERE restaurant_id=" + restaurant_id;
	        Statement statement = database.getConnection().createStatement();
	        ResultSet result = statement.executeQuery(sql);
	        database.disconnect();
	        if (result.next())
	        	return (int)Float.parseFloat(result.getString("average"));
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}
	    return -1;
	}
	
	public boolean updateRating(long id, int average) {
		try {
			DatabaseManager database = new DatabaseManager();
			String sql = "UPDATE public.\"Restaurant\" SET "
				+ "rating = ? WHERE id = ?";
			PreparedStatement statement = database.getConnection().prepareStatement(sql);
			statement.setInt(1, average);
			statement.setLong(2, id);
			statement.executeUpdate();
			database.disconnect();
			
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
		return true;
	}
	
	public boolean insertReview(Review review, User user) {
		int average;
		String sql;
		if (review.getMessage() == null)
			sql = "INSERT INTO public.\"Review\" (user_id, restaurant_id, rating) "
	    			+ "VALUES (?, ?, ?)";
		else	
			sql = "INSERT INTO public.\"Review\" (user_id, restaurant_id, message, rating) "
    			+ "VALUES (?, ?, ?, ?)";
		DatabaseManager database = new DatabaseManager();
		boolean answer = false;
		try {
	        PreparedStatement statement = database.getConnection().prepareStatement(sql);
	        statement.setLong(1, user.getId());
	        statement.setLong(2, review.getRestaurant_id());
	        if (review.getMessage() != null) {
	        	statement.setString(3, review.getMessage());
	        	statement.setInt(4, review.getRating());
	        } else
	        	statement.setInt(3, review.getRating());
	        if(statement.executeUpdate() == 1)
	        	answer = true;
	        database.disconnect();
	        average = (int)getAverage(review.getRestaurant_id());
	        if (average != -1) 
	        	updateRating(review.getRestaurant_id(), average);
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}
	    return answer;   
	}
	
	public boolean updateReview(Review review) {
		String sql = makeQuery(review);
		if (sql == null)
			return false;
		DatabaseManager database = new DatabaseManager();
		boolean answer = false;
		try {
	        PreparedStatement statement = database.getConnection().prepareStatement(sql);
	        if(statement.executeUpdate() == 1)
	        	answer = true;
	        database.disconnect();
	        int average = (int)getAverage(review.getRestaurant_id());
	        if (average != -1) 
	        	updateRating(review.getRestaurant_id(), average);
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}	
	    return answer;   
	}
	
	public String makeQuery(Review review) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String query = "UPDATE public.\"Review\" SET ";
		if (review.getMessage() != null)
			map.put("message", review.getMessage());
		if (review.getRating() >= 0)
			map.put("rating", review.getRating());
		Iterator<Entry<String, Object>> it =  map.entrySet().iterator();
		if (!it.hasNext())
			return null;
		while (it.hasNext()) {
			Entry<String, Object> pair = it.next();
			query += pair.getKey() + "='" + pair.getValue()+ "'";
			if (it.hasNext())
				query += ", ";
		}
		query += " WHERE id=" + review.getId() + ";";
		return query;
	}
	
	public boolean deleteReview(int id) {
		String sql = "DELETE FROM public.\"Review\" WHERE id = ?";
		DatabaseManager database = new DatabaseManager();
		boolean answer = false;
		try {
	        PreparedStatement statement = database.getConnection().prepareStatement(sql);
	        statement.setLong(1, id);
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
