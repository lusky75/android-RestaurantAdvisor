package etna.webservice.jersey.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import etna.webservice.jersey.model.Menu;

public class MenuDAO {
	public ArrayList<Menu> getAll() {
		Menu menu = null;
		ArrayList<Menu> list = new ArrayList<Menu>();
		DatabaseManager database = new DatabaseManager();
		String query = "SELECT * FROM public.\"Menu\"";
	    try {
	    	Statement statement = database.getConnection().createStatement ();
	    	ResultSet rs = statement.executeQuery (query);

	    	while ( rs.next() ) {
	    		menu = new Menu();
	    		menu.setId(rs.getLong("id"));
	    		menu.setName(rs.getString("name"));
	    		menu.setDescription(rs.getString("description"));
	    		menu.setPrice(rs.getFloat("price"));
	    		menu.setRestaurant_id(rs.getLong("restaurant_id"));
	    		list.add(menu);
	    	}
	        database.disconnect();
	    } catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	    }
	    return (list);
	}
	
	public ArrayList<Menu> getByRestaurantId(long id) {
		Menu menu = null;
		ArrayList<Menu> list = new ArrayList<Menu>();
		DatabaseManager database = new DatabaseManager();
		String query = "SELECT * FROM public.\"Menu\" WHERE restaurant_id = ?";
	    try {
	    	PreparedStatement statement = database.getConnection().prepareStatement (query);
	    	statement.setLong(1, id);
	    	ResultSet rs = statement.executeQuery();

	    	if (!rs.isBeforeFirst())
	    		return null;
	    	while ( rs.next() ) {
	    		menu = new Menu();
	    		menu.setId(rs.getLong("id"));
	    		menu.setName(rs.getString("name"));
	    		menu.setDescription(rs.getString("description"));
	    		menu.setPrice(rs.getFloat("price"));
	    		menu.setRestaurant_id(id);
	    		list.add(menu);
	    	}
	        database.disconnect();
	    } catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	    }
	    return (list);
	}
	
	public Menu getById(long id) {
		Menu menu = null;
		DatabaseManager database = new DatabaseManager();
		String query = "SELECT * FROM public.\"Menu\" WHERE id = ?";
	    try {
	    	PreparedStatement statement = database.getConnection().prepareStatement (query);
	    	statement.setLong(1, id);
	    	ResultSet rs = statement.executeQuery ();
	    	if(!rs.next())
	    		return null;
	    	menu = new Menu();
	    	menu.setId(id);
	    	menu.setName(rs.getString("name"));
	   		menu.setDescription(rs.getString("description"));
	   		menu.setPrice(rs.getFloat("price"));
	   		menu.setRestaurant_id(rs.getLong("restaurant_id"));
	        database.disconnect();
	    } catch (java.sql.SQLException e) {
	         System.err.println (e);
	         System.exit (-1);
	    }
	    return (menu);
	}
	
	public boolean insertMenu(Menu menu) {
		String sql = "INSERT INTO public.\"Menu\" (name, price, restaurant_id) "
    			+ "VALUES (?, ?, ?)";
		DatabaseManager database = new DatabaseManager();
		boolean answer = false;
		try {
	        PreparedStatement statement = database.getConnection().prepareStatement(sql);
	        statement.setString(1, menu.getName());
	        statement.setFloat(2, menu.getPrice());
	        statement.setLong(3, menu.getRestaurant_id());
	        if(statement.executeUpdate() == 1)
	        	answer = true;
	        database.disconnect();
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}
	    return answer;   
	}
	
	public boolean updateMenu(Menu menu) {
		String query = makeQuery(menu);
		if (query == null)
			return false;
		DatabaseManager database = new DatabaseManager();
		boolean answer = false;
		try {
	        PreparedStatement statement = database.getConnection().prepareStatement(query);
	        if(statement.executeUpdate() == 1)
	        	answer = true;
	        database.disconnect();
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}	
	    return answer;   
	}
	
	public String makeQuery(Menu menu) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String query = "UPDATE public.\"Menu\" SET ";
		if (menu.getName() != null)
			map.put("name", menu.getName());
		if (menu.getDescription() != null)
			map.put("description", menu.getDescription());
		if (menu.getPrice() != 0)
			map.put("price", menu.getPrice());
		if (menu.getRestaurant_id() != 0)
			map.put("restaurant_id", menu.getRestaurant_id());
		Iterator<Entry<String, Object>> it =  map.entrySet().iterator();
		if (!it.hasNext())
			return null;
		while (it.hasNext()) {
			Entry<String, Object> pair = it.next();
			query += pair.getKey() + "='" + pair.getValue()+ "'";
			if (it.hasNext())
				query += ", ";
		}
		query += " WHERE id=" + menu.getId() + ";";
		return query;
	}
	
	public boolean deleteMenu(Menu menu) {
		String sql = "DELETE FROM public.\"Menu\" WHERE id = ?";
		DatabaseManager database = new DatabaseManager();
		boolean answer = false;
		try {
	        PreparedStatement statement = database.getConnection().prepareStatement(sql);
	        statement.setLong(1, menu.getId());
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
