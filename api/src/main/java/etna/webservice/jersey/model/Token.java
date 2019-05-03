package etna.webservice.jersey.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import etna.webservice.jersey.dao.UserDAO;

public class Token {

	public String issueToken(User user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256("secret");
			String token = JWT.create().withIssuer("webservice").withSubject(user.getUsername())
					.withClaim("name", user.getFirstname() + " " + user.getLastname())
					.withClaim("admin", user.isAdmin()).sign(algorithm);
			return token;
		} catch (JWTCreationException exception) {
		}
		return null;
	}

	public User getTokenUser(String token) {
		User user = new User();
		UserDAO userdao = new UserDAO();
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256("secret")).withIssuer("webservice").build();
		DecodedJWT jwt = verifier.verify(token);
		user = userdao.getByUsername(jwt.getClaim("sub").asString());
		
		return user;
	}

	public boolean verifyToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256("secret");
			JWTVerifier verifier = JWT.require(algorithm).withIssuer("webservice").build();
			DecodedJWT jwt = verifier.verify(token);
		} catch (JWTVerificationException exception) {
			return false;
		}
		return true;
	}
}
