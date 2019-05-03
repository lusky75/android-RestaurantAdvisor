package etna.webservice.jersey.model;

public class User {
    
    private long id;
    private String username;
    private String password;
    private boolean admin;
    private String firstname;
    private String lastname;
    private String email;
    private String city;
    private String country;
    private String birthday;

    public User(){
        
    }
    
    public User(long id, String username, String password, boolean admin, String firstname, String lastname, String email,
            String city, String country, String birthday) {
    	this.id = id;
        this.username = username;
        this.password = password;
        this.admin = admin;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.city = city;
        this.country = country;
        this.birthday = birthday;
    }
    
    public User(String username, String password, boolean admin, String firstname, String lastname, String email,
            String city, String country, String birthday) {
        this.username = username;
        this.password = password;
        this.admin = admin;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.city = city;
        this.country = country;
        this.birthday = birthday;
    }
    
    public void setUpdate(String firstname, String lastname, String email, String city, String country, String birthday) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.city = city;
        this.country = country;
        this.birthday = birthday;
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
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setCredentials(String username, String password) {
    	this.username = username;
    	this.password = password;
    }
    
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String state) {
        this.country = state;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

}