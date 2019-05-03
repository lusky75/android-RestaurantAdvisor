package etna.webservice.jersey.model;

import java.util.ArrayList;

public class Restaurant {
	private long id;
	private String name;
	private String description;
	private String location;
	private String phone;
	private String weekhour;
	private String weekendhour;
	private String website;
	private int rating;
	private ArrayList<Menu> menu;
	
	public Restaurant() {
		
	}
	
	public Restaurant(long id, String name, String description, String location, String phone, String weekhour, String weekendhour, String website) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.location = location;
		this.phone = phone;
		this.weekhour = weekhour;
		this.weekendhour = weekendhour;
		this.website = website;
	}
	
	public Restaurant(String name, String description, String location, String phone, String weekhour, String weekendhour, String website) {
		this.name = name;
		this.description = description;
		this.location = location;
		this.phone = phone;
		this.weekhour = weekhour;
		this.weekendhour = weekendhour;
		this.website = website;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getWeekHour() {
		return weekhour;
	}
	
	public void setWeekHour(String weekHour) {
		this.weekhour = weekHour;
	}
	
	public String getWeekEndHour() {
		return weekendhour;
	}
	
	public void setWeekEndHour(String weekEndHour) {
		this.weekendhour = weekEndHour;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ArrayList<Menu> getMenu() {
		return menu;
	}

	public void setMenu(ArrayList<Menu> menu) {
		this.menu = menu;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
}
