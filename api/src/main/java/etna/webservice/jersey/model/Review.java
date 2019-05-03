package etna.webservice.jersey.model;

public class Review {
	private long id;
	private long user_id;
	private long restaurant_id;
	private String username;
	private String restaurant;
	private String message;
	private int rating;
	
	public Review() {
		
	}
	
	public Review(long id, long user_id, long restaurant_id, int rating, String message) {
		this.setId(id);
		this.user_id = user_id;
		this.restaurant_id = restaurant_id;
		this.rating = rating;
		this.message = message;
	}
	
	public Review(long user_id, long restaurant_id, int rating, String message) {
		this.user_id = user_id;
		this.restaurant_id = restaurant_id;
		this.rating = rating;
		this.message = message;
	}
	
	public Review(long id, int rating, String message) {
		this.id = id;
		this.rating = rating;
		this.message = message;
	}
	
	public long getUser_id() {
		return user_id;
	}
	
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	
	public long getRestaurant_id() {
		return restaurant_id;
	}
	
	public void setRestaurant_id(long restaurant_id) {
		this.restaurant_id = restaurant_id;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}
}
