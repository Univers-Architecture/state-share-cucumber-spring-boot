package com.univers.architecture.cucumber.feature.currentuser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.univers.architecture.cucumber.step.AbstractBaseStepDefinition;
import com.univers.architecture.cucumber.step.auth.AuthStepWorld;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * This class contains not shared steps for current user feature.
 * 
 * @author Issam SABIR
 */
public class CurrentUserStepsDefinitions extends AbstractBaseStepDefinition {

	private final Logger log = LoggerFactory.getLogger(CurrentUserStepsDefinitions.class);
	private HttpResponse lastHttpResponse;
	private User currentUser;

	@Autowired
	private AuthStepWorld stepWorld;

	@When("^I ask for the current user calling \"([^\"]*)\"$")
	public void requestCurrentUser(final String currentUserApiUrl) throws IOException {

		log.info("Calling url in GET mode: {}", currentUserApiUrl);
		HttpUriRequest request = new HttpGet(currentUserApiUrl);
		request.addHeader("x-auth-token", stepWorld.getJwtToken().getToken());
		lastHttpResponse = HttpClientBuilder.create().build().execute(request);

		// On extrait le user courant si possible
		if (lastHttpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
			currentUser = this.retrieveResourceFromResponse(lastHttpResponse, User.class);
		}
	}

	@Then("^I get (\\w+) response$")
	public void checkLastResponseSatus(String responseSatus) {
		log.info("Checking last reponse status is {}", responseSatus);
		assertThat(lastHttpResponse.getStatusLine().getStatusCode())
				.isEqualTo(HttpStatus.valueOf(responseSatus).value());
	}

	@Then("^the user detail must contains:$")
	public void checkUserDetail(DataTable userDetail) throws Exception {
		Map<String, String> detailAsMap = userDetail.asMap(String.class, String.class);
		assertThat(detailAsMap.get("login")).isEqualTo(currentUser.getLogin());
		assertThat(detailAsMap.get("firstName")).isEqualTo(currentUser.getFirstName());
		assertThat(detailAsMap.get("email")).isEqualTo(currentUser.getEmail());
		assertThat(detailAsMap.get("house")).isEqualTo(currentUser.getHouse());
		assertThat(detailAsMap.get("activated")).isEqualTo(currentUser.getActivated());
	}

}
