/**
 * 
 */
package com.univers.architecture.cucumber.step.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.univers.architecture.cucumber.SpringBootCucumberApplication;
import com.univers.architecture.cucumber.step.AbstractBaseStepDefinition;

import cucumber.api.java.en.Given;

/**
 * Every shared step must manage its own and independent World.
 * 
 * Every other step that depends on it must inject the World bean. No no
 * dependency must exist between steps belonging to different scenarios.
 * 
 * The Spring context must be loaded only one time, by one step.
 * 
 * @author Issam SABIR.
 *
 */
// This annotation is present only here for this step
@ContextConfiguration(classes = SpringBootCucumberApplication.class)
public class AuthStepDefinition extends AbstractBaseStepDefinition {

	private final Logger log = LoggerFactory.getLogger(AuthStepDefinition.class);

	@Autowired
	private AuthStepWorld stepWorld;

	@Given("^I'm authentified with user (.*) and password (.*) on the url (.*)$")
	public void authenticateUser(String username, String pasword, String authUrl) throws Exception {

		CloseableHttpResponse reponse = this.doAuthenticateUser(username, pasword, authUrl);

		stepWorld.setReponse(reponse);

		JwtToken tocken = this.retrieveResourceFromResponse(reponse, JwtToken.class);

		stepWorld.setJwtToken(tocken);

	}

	/**
	 * Do authenticate user.
	 *
	 * @param username
	 *            the username
	 * @param pasword
	 *            the pasword
	 * @param authUrl
	 *            the auth url
	 * @return the closeable http response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private CloseableHttpResponse doAuthenticateUser(String username, String pasword, String authUrl)
			throws IOException {

		log.info("Authenticate user, calling url in POST mode: {}", authUrl);

		HttpPost httpPost = new HttpPost(authUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", pasword));
		httpPost.setEntity(new UrlEncodedFormEntity(params));

		CloseableHttpResponse reponse = HttpClientBuilder.create().build().execute(httpPost);
		return reponse;
	}

}
