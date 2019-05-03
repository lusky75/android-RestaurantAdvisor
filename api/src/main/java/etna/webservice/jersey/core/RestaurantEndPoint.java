package etna.webservice.jersey.core;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import etna.webservice.jersey.dao.RestaurantDAO;
import etna.webservice.jersey.model.EntityBuilder;
import etna.webservice.jersey.model.Restaurant;
import etna.webservice.jersey.model.Token;

@Path("restau")
@Produces(MediaType.APPLICATION_JSON)
public class RestaurantEndPoint {

	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response print() {
		EntityBuilder answer = new EntityBuilder();
		RestaurantDAO restaurantdao = new RestaurantDAO();
		ArrayList<Restaurant> list = restaurantdao.getAll();
		return Response.ok(answer.set(true, "Restaurants list", null, list).getMap()).build();
	}
	
	@GET
	@Path("/list/id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response displayById(@HeaderParam("token") String token, @PathParam("id") int id) {
		EntityBuilder answer = new EntityBuilder();
		RestaurantDAO restaurantdao = new RestaurantDAO();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
		Restaurant restaurant = restaurantdao.getById(id);
		if (restaurant == null) {
			return Response.status(404).entity(answer.set(false, "Item not found", token, null).getMap()).build();
		}
		return Response.ok(answer.set(true, "Item found", token, restaurant).getMap()).build();
	}
	
	@GET
	@Path("/list/name/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchRestaurant(@PathParam("name") String name) {
		EntityBuilder answer = new EntityBuilder();
		RestaurantDAO restaurantdao = new RestaurantDAO();
		ArrayList<Restaurant> list = restaurantdao.getByName(name);
		if (list == null) {
			return Response.status(404).entity(answer.set(false, "Item not found", null, null).getMap()).build();
		}
		return Response.ok(answer.set(true, "Research by name", null, list).getMap()).build();
	}
	
	@POST
	@Path("/insert")
	@Produces(MediaType.APPLICATION_JSON)
	public Response insert(@HeaderParam("token") String token, 
							@HeaderParam("name") String name,
							@HeaderParam("description") String description,
							@HeaderParam("location") String location,
							@HeaderParam("phone") String phone,
							@HeaderParam("website") String website,
							@HeaderParam("weekhour") String weekhour,
							@HeaderParam("weekendhour") String weekendhour) {
		Restaurant restau = new Restaurant(name, description, location, phone,  weekhour, weekendhour, website);
		RestaurantDAO restaurantdao = new RestaurantDAO();
		EntityBuilder answer = new EntityBuilder();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
        if (!jwt.getTokenUser(token).isAdmin())
        	return Response.status(401).entity(answer.set(false, "Only admin can manage resources", token, null).getMap()).build();
        if (name == null || name.isBlank() || location == null || location.isBlank())
        	return Response.status(400).entity(answer.set(false, "Restaurant insertion requires at least 'name' and 'location' query params", token, null).getMap()).build();
		if (restaurantdao.insertRestaurant(restau)) {
			restaurantdao.updateRestaurant(restau);
			return Response.status(201).entity(answer.set(true, "Restaurant created", token, restau).getMap()).build();
		}
		return Response.status(404).entity("fail").build();
	}
	
	@PUT
	@Path("update/{id}")
	public Response update(@HeaderParam("token") String token, @PathParam("id") long id,
							@QueryParam("name") String name,
							@QueryParam("description") String description,
							@QueryParam("location") String location,
							@QueryParam("phone") String phone,
							@QueryParam("website") String website,
							@QueryParam("weekhour") String weekhour,
							@QueryParam("weekendhour") String weekendhour) {
		Restaurant restau = new Restaurant(id, name, description, location, phone,  weekhour, weekendhour, website);
		RestaurantDAO restaurantdao = new RestaurantDAO();
		EntityBuilder answer = new EntityBuilder();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
        if (!jwt.getTokenUser(token).isAdmin())
        	return Response.status(401).entity(answer.set(false, "Admin only", token, null).getMap()).build();
        if (restaurantdao.getById(id) == null)
        	return Response.status(404).entity(answer.set(false, "No Restaurant for this id", token, null).getMap()).build();
		if (restaurantdao.updateRestaurant(restau)) {
			return Response.status(200).entity(answer.set(false, "Restaurant updated", token, restaurantdao.getById(id)).getMap()).build();
		}
        return Response.status(404).entity(answer.set(false, "No update sent", token, null).getMap()).build();
	}
	
	@DELETE
	@Path("delete/{id}")
	public Response delete(@HeaderParam("token") String token, @PathParam("id") long id) {
		Restaurant restaurant = new Restaurant();
		RestaurantDAO restaurantdao = new RestaurantDAO();
		EntityBuilder answer = new EntityBuilder();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
        if (!jwt.getTokenUser(token).isAdmin())
        	return Response.status(401).entity(answer.set(false, "Admin only", token, null).getMap()).build();
		restaurant = restaurantdao.getById(id);
		if (restaurantdao.deleteRestaurant(restaurant)) {
			return Response.ok(answer.set(true, "Restaurant successfully removed", token, null).getMap()).build();
		}
		return Response.status(404).entity(answer.set(true, "ID not found", token, null).getMap()).build();
	}
}

