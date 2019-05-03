package etna.webservice.jersey.core;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import etna.webservice.jersey.dao.UserDAO;
import etna.webservice.jersey.model.EntityBuilder;
import etna.webservice.jersey.model.Token;
import etna.webservice.jersey.model.User;

@Path("/users")
public class UserEndPoint {

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(@HeaderParam("username") String username, @HeaderParam("password") String password, 
			@HeaderParam("firstname") String firstname, @HeaderParam("lastname") String lastname, 
			@HeaderParam("email") String email ,@HeaderParam("city") String city, 
			@HeaderParam("country") String country, @HeaderParam("birthday") String birthday) {
		User user = new User(username, password, false, firstname, lastname, email, city, country, birthday);
		UserDAO userdao = new UserDAO();
		EntityBuilder answer = new EntityBuilder();
		if (username == null || password == null)
			return Response.status(400).entity(answer.set(false, " 'username' and 'password' header keys required", null, null).getMap()).build();
		System.out.print(username.length());
		if (username.length() < 4 || password.length() < 4 || username.isBlank() || password.isBlank())
			return Response.status(404).entity(answer.set(false, "Username and password must contain at least four characters", null, null).getMap()).build();
		if (!userdao.userExists(user)) {
			userdao.insertUser(user);
			userdao.updateUser(user);
			user.setPassword("hidden");
			return Response.status(201).entity(answer.set(true, "Successfully registered", null, user).getMap()).build();
		}
		return Response.status(409).entity(answer.set(false, "Registration failed", null, null).getMap()).build();
	}
	
	@POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(@HeaderParam("username") String username,
                                     @HeaderParam("password") String password) {
		EntityBuilder answer = new EntityBuilder();
		UserDAO userdao = new UserDAO();
		User user = new User();
		Token jwt = new Token();
		if (username == null || password == null)
			return Response.status(400).entity(answer.set(false, " 'username' and 'password' header keys required", null, null).getMap()).build();
		user.setCredentials(username, password);
        try {
        	if (userdao.userExists(user) && userdao.authUser(user)) {
        		String token = jwt.issueToken(user);
        		user = userdao.getByUsername(username);
        		return Response.ok(answer.set(true, "Authentification successful", token, user).getMap()).build();
        	}
        	else
        		return Response.status(401).entity(answer.set(false, "Authentification failed", null, null).getMap()).build();
        } catch (Exception e) {
           
        }
		return null;
    }
	
	@GET
    @Path("/profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userProfile(@HeaderParam("token") String token) {
		EntityBuilder answer = new EntityBuilder();
		User user = new User();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
        user = jwt.getTokenUser(token);
       
		return Response.ok(answer.set(true, "User profile", token, user).getMap()).build();
    }
	
	@PUT
	@Path("/profile/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@HeaderParam("token") String token, @HeaderParam("firstname") String firstname, @HeaderParam("lastname") String lastname, 
			@HeaderParam("email") String email ,@HeaderParam("city") String city, 
			@HeaderParam("country") String country, @HeaderParam("birthday") String birthday) {
		User user = new User();
		UserDAO userdao = new UserDAO();
		EntityBuilder answer = new EntityBuilder();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
        user = jwt.getTokenUser(token);
        user.setUpdate(firstname, lastname, email, city, country, birthday);
		if (userdao.updateUser(user)) {
			user = userdao.getByUsername(user.getUsername());
			return Response.ok(answer.set(true, "Profile updated", token, user).getMap()).build();
		}
		return Response.status(400).entity(answer.set(false, "Update failed", token, null).getMap()).build();
	}
	
	@DELETE
	@Path("profile/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@HeaderParam("token") String token) {
		User user = new User();
		UserDAO userdao = new UserDAO();
		EntityBuilder answer = new EntityBuilder();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
		user = jwt.getTokenUser(token);
		if (userdao.deleteUser(user)) {
			return Response.ok(answer.set(true, "Profile successfully deleted", token, null).getMap()).build();
		}
		return Response.status(404).entity(answer.set(true, "Profile deletion failed", token, null).getMap()).build();
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listUsers(@HeaderParam("token") String token) {
		UserDAO userdao = new UserDAO();
		User user = new User();
		EntityBuilder answer = new EntityBuilder();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
        user = jwt.getTokenUser(token);
        if (!user.isAdmin())
        	return Response.status(401).entity(answer.set(false, "Admin only", token, null).getMap()).build();
        return Response.ok(answer.set(true, "Users list", token, userdao.getAll()).getMap()).build();
	}
	
	@DELETE
	@Path("delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@HeaderParam("token") String token, @PathParam("id") long id) {
		User user = new User();
		UserDAO userdao = new UserDAO();
		EntityBuilder answer = new EntityBuilder();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
        if (!jwt.getTokenUser(token).isAdmin())
        	return Response.status(401).entity(answer.set(false, "Admin only", token, null).getMap()).build();
		user = userdao.getById(id);
		if (user == null)
			return Response.status(404).entity(answer.set(true, "ID not found", token, null).getMap()).build();
		if (userdao.deleteUser(user)) {
			return Response.ok(answer.set(true, "User successfully removed", token, null).getMap()).build();
		}
		return Response.status(404).entity(answer.set(true, "Deletion failed", token, null).getMap()).build();
	}
}
