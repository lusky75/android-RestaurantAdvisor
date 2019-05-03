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

import etna.webservice.jersey.dao.MenuDAO;
import etna.webservice.jersey.dao.RestaurantDAO;
import etna.webservice.jersey.model.EntityBuilder;
import etna.webservice.jersey.model.Menu;
import etna.webservice.jersey.model.Token;

@Path("menu")
@Produces(MediaType.APPLICATION_JSON)
public class MenuEndPoint {

	@GET
	@Path("/list")
	public Response print() {
		MenuDAO menudao = new MenuDAO();
		ArrayList<Menu> list = menudao.getAll();
		EntityBuilder answer = new EntityBuilder();
		
		return Response.ok(answer.set(true, "List of all menus", null, list).getMap()).build();
	}
	
	@GET
	@Path("/list/{id}")
	public Response displayByRestaurantId(@PathParam("id") long id) {
		MenuDAO menudao = new MenuDAO();
		RestaurantDAO restaudao = new RestaurantDAO();
		ArrayList<Menu> list = menudao.getByRestaurantId(id);
		EntityBuilder answer = new EntityBuilder();
		if (restaudao.getById(id) == null)
			return Response.status(400).entity(answer.set(false, "No restaurant for this id", null, null)).build();
		return Response.ok(answer.set(true, "Menu of "+ restaudao.getById(id).getName(), null, list).getMap()).build();
	}
	
	@POST
	@Path("/insert")
	public Response insert(@HeaderParam("token") String token, @QueryParam("name") String name, @QueryParam("description") String description,
							@QueryParam("price") float price, @QueryParam("restaurant_id") long restaurant_id) {
		RestaurantDAO restaurantdao = new RestaurantDAO();
		Menu menu = new Menu(name, description, price, restaurant_id);
		MenuDAO menudao = new MenuDAO();
		EntityBuilder answer = new EntityBuilder();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
	    if (!jwt.getTokenUser(token).isAdmin())
	    	return Response.status(401).entity(answer.set(false, "Admin only", token, null).getMap()).build();
	    if (menu.getName() == null || menu.getRestaurant_id() <= 0)
	    	return Response.status(400).entity(answer.set(false, "Query param 'restaurant_id' greater than 0 and query param 'name' required", token, null).getMap()).build();
	    if (restaurantdao.getById(restaurant_id) == null)
	    	return Response.status(400).entity(answer.set(false, "No restaurant for this id", token, null).getMap()).build();
		if (menudao.insertMenu(menu)) {
			return Response.status(201).entity(answer.set(true, "Menu created", null, menu).getMap()).build();
		}
		return Response.status(404).entity(answer.set(false, "Fail", token, null).getMap()).build();
	}
	
	@PUT
	@Path("update/{id}")
	public Response update(@HeaderParam("token") String token, @PathParam("id") long id,
							@QueryParam("name") String name,
							@QueryParam("description") String description,
							@QueryParam("price") float price, @QueryParam("restaurant_id") long restaurant_id) {
		Menu menu = new Menu(id, name, description, price, restaurant_id);
		MenuDAO menudao = new MenuDAO();
		EntityBuilder answer = new EntityBuilder();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
		if (!jwt.getTokenUser(token).isAdmin())
	    	return Response.status(401).entity(answer.set(false, "Admin only", token, null).getMap()).build();
		if (menudao.getById(id) == null)
			return Response.status(400).entity(answer.set(false, "No menu for this id", token, null)).build();
		if (menudao.updateMenu(menu)) {
			menu = menudao.getById(id);
			return Response.status(200).entity(answer.set(true, "Menu updated", token, menu)).build();
		}
		return Response.status(404).entity(answer.set(false, "No update sent", token, null)).build();
	}
	
	@DELETE
	@Path("delete/{id}")
	public Response delete(@HeaderParam("token") String token, @PathParam("id") long id) {
		Menu menu = new Menu();
		MenuDAO menudao = new MenuDAO();
		EntityBuilder answer = new EntityBuilder();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
		if (!jwt.getTokenUser(token).isAdmin())
	    	return Response.status(401).entity(answer.set(false, "Admin only", token, null).getMap()).build();
		menu = menudao.getById(id);
		if (menu == null)
			return Response.status(400).entity(answer.set(false, "No menu for this id", token, null).getMap()).build();
		if (menudao.deleteMenu(menu)) {
			String message = "Menu deleted";
			return Response.ok(answer.set(true, message, token, null).getMap()).build();
		}
		return Response.status(404).entity(answer.set(false, "Deletion failed", token, null).getMap()).build();
	}
}
