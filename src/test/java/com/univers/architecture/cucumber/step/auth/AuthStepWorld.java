/**
 * 
 */
package com.univers.architecture.cucumber.step.auth;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A World class allow sharing state between steps of the same scenario. No no
 * dependency must exist between steps belonging to different scenarios.
 * 
 * Never forget to use the 'cucumber-scope' as mentioned below.
 * 
 * The 'cucumber-scope' is to use only here.
 * 
 * @author Issam SABIR
 *
 */
@Component
@Scope("cucumber-glue")
public class AuthStepWorld {

	private final Logger log = LoggerFactory.getLogger(AuthStepWorld.class);

	private CloseableHttpResponse reponse;
	private JwtToken jwtToken = new JwtToken();

	public AuthStepWorld() {
		super();
		log.info("Creating new world: {}", AuthStepWorld.class);
	}

	public CloseableHttpResponse getReponse() {
		return reponse;
	}

	/**
	 * Not to be public, to avoid any World change outside this package
	 *
	 * @param reponse
	 *            the new reponse
	 */
	void setReponse(CloseableHttpResponse reponse) {
		this.reponse = reponse;
	}

	public JwtToken getJwtToken() {
		return jwtToken;
	}

	/**
	 * Not to be public, to avoid any World change outside this package
	 *
	 * @param jwtToken
	 *            the new jwt token
	 */
	void setJwtToken(JwtToken jwtToken) {
		this.jwtToken = jwtToken;
	}

}
