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
import etna.webservice.jersey.dao.ReviewDAO;
import etna.webservice.jersey.model.EntityBuilder;
import etna.webservice.jersey.model.Restaurant;
import etna.webservice.jersey.model.Review;
import etna.webservice.jersey.model.Token;
import etna.webservice.jersey.model.User;

@Path("reviews")
@Produces(MediaType.APPLICATION_JSON)
public class ReviewEndPoint {
	
	@GET
	public Response getUserReviews(@HeaderParam("token") String token) {
		EntityBuilder answer = new EntityBuilder();
		ReviewDAO reviewdao = new ReviewDAO();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap())
					.build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
		User user = jwt.getTokenUser(token);
		ArrayList<Review> reviews = reviewdao.getByUserId(user.getId());
		return Response.ok(answer.set(true, user.getUsername()+"'s reviews", token, reviews).getMap()).build();		
	}
	
	@GET
	@Path("/restau/{restaurant_id}")
	public Response getReviewsOfRestaurant(@HeaderParam("token") String token, @PathParam("restaurant_id") long restaurant_id) {
		EntityBuilder answer = new EntityBuilder();
		ReviewDAO reviewdao = new ReviewDAO();
		RestaurantDAO restaudao = new RestaurantDAO();
		Restaurant restau = restaudao.getById(restaurant_id);
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap())
					.build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
		if (restaurant_id <= 0)
			return Response.status(400).entity(answer.set(false, "QueryParam 'restaurant_id' greater than 0 required", token, null).getMap()).build();
		if (restau == null)
	    	return Response.status(400).entity(answer.set(false, "No restaurant for this id", token, null).getMap()).build();
		ArrayList<Review> reviews = reviewdao.getByRestaurantId(restau.getId());
		if (reviews != null)
			return Response.ok(answer.set(true, "Restaurant: " + restau.getName()+ ", average raiting: " + restau.getRating(), token, reviews).getMap()).build();		
		return Response.status(404).entity(answer.set(false, "Failed getting resources", token, null ).getMap()).build();
	}
	
	@POST
	@Path("add")
	public Response insert(@HeaderParam("token") String token,
							@QueryParam("restaurant_id") long restaurant_id,
							@QueryParam("message") String message,
							@QueryParam("rating") int rating) {
		EntityBuilder answer = new EntityBuilder();
		RestaurantDAO restaudao = new RestaurantDAO();
		ReviewDAO reviewdao = new ReviewDAO();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
		User user = jwt.getTokenUser(token);
		if (restaurant_id <= 0)
			return Response.status(400).entity(answer.set(false, "QueryParam 'restaurant_id' greater than 0 required", token, null).getMap()).build();
		if (rating > 5 || rating < 0)
			return Response.status(400).entity(answer.set(false, "QueryPram 'rating' must be an integer between 0 and 5", token, null).getMap()).build();
		if (restaudao.getById(restaurant_id) == null)
	    	return Response.status(400).entity(answer.set(false, "No restaurant for this id", token, null).getMap()).build();
		Review review = new Review(user.getId(), restaurant_id, rating, message);
		if (reviewdao.reviewExists(user, review))
			return Response.status(400).entity(answer.set(false, user.getUsername() + " already reviewed this restaurant. Try PUT /review/update/{id} to modify or DELETE /review/delete/{id} to delete", token, review).getMap()).build();
		if (reviewdao.insertReview(review, user)) {
			return Response.status(201).entity(answer.set(true, "Review created", token, review).getMap()).build();
		}
		return Response.status(404).entity(answer.set(false, "Review creation failed", token, null ).getMap()).build();
	}
	
	@PUT
	@Path("update/{id}")
	public Response update(@PathParam("id") long id, @HeaderParam("token") String token, 
			@QueryParam("message") String message, @QueryParam("rating") int rating) {
		EntityBuilder answer = new EntityBuilder();
		ReviewDAO reviewdao = new ReviewDAO();
		Review review = reviewdao.getById(id);
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
		if (review == null)
			return Response.status(400).entity(answer.set(false, "No review for this id", token, null).getMap()).build();
		User user = jwt.getTokenUser(token);
		if (review.getUser_id() != user.getId())
			return Response.status(401).entity(answer.set(false, "Review owned by a different user", token, null).getMap()).build();
		review.setMessage(message);
		review.setRating(rating);
		if (reviewdao.updateReview(review))
			return Response.status(200).entity(answer.set(true, "Review updated", token, reviewdao.getById(id)).getMap()).build();
		return Response.status(404).entity(answer.set(true, "No update made", token, reviewdao.getById(id)).getMap()).build();
	}
	
	@DELETE
	@Path("delete/{id}")
	public Response delete(@PathParam("id") int id, @HeaderParam("token") String token) {
		ReviewDAO reviewdao = new ReviewDAO();
		Review review = reviewdao.getById(id);
		EntityBuilder answer = new EntityBuilder();
		Token jwt = new Token();
		if (token == null)
			return Response.status(400).entity(answer.set(false, "Token required in header", null, null).getMap()).build();
		if (!jwt.verifyToken(token))
			return Response.status(401).entity(answer.set(false, "Invalid Token", token, null).getMap()).build();
		if (review == null)
			return Response.status(400).entity(answer.set(false, "No review for this id", token, null).getMap()).build();
		User user = jwt.getTokenUser(token);
		if (review.getUser_id() != user.getId() || !user.isAdmin())
			return Response.status(401).entity(answer.set(false, "Review owned by a different user", token, null).getMap()).build();
		if (reviewdao.deleteReview(id)) {
			return Response.status(200).entity(answer.set(true, "Review deleted", token, null).getMap()).build();
		}
		return Response.status(404).entity("fail").build();
	}
	
}

