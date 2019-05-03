package etna.webservice.jersey.model;

public class Menu {
	private long id;
	private String name;
	private String description;
	private float price;
	private long restaurant_id;
	
	public Menu( ) {
		
	}
	
	public Menu(long id, String name, String description, float price, long restaurant_id) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.restaurant_id = restaurant_id;
	}
	
	public Menu(String name, String description, float price, long restaurant_id) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.restaurant_id = restaurant_id;
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
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRestaurant_id() {
		return restaurant_id;
	}

	public void setRestaurant_id(long restaurant_id) {
		this.restaurant_id = restaurant_id;
	}
}
