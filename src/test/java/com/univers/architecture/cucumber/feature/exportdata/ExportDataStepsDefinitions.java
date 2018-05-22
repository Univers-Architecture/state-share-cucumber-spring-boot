package com.univers.architecture.cucumber.feature.exportdata;

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
 * This class contains not shared steps for export data feature.
 * 
 * @author Issam SABIR
 */
public class ExportDataStepsDefinitions extends AbstractBaseStepDefinition {

	private final Logger log = LoggerFactory.getLogger(ExportDataStepsDefinitions.class);
	private HttpResponse lastHttpResponse;
	private ExportedData exportedData;

	@Autowired
	private AuthStepWorld stepWorld;

	@When("^I request data export by calling \"([^\"]*)\"$")
	public void requestDataExport(final String currentUserApiUrl) throws IOException {

		log.info("Calling url in GET mode: {}", currentUserApiUrl);
		HttpUriRequest request = new HttpGet(currentUserApiUrl);
		request.addHeader("x-auth-token", stepWorld.getJwtToken().getToken());
		lastHttpResponse = HttpClientBuilder.create().build().execute(request);

		// Extracting exported data if possible
		if (lastHttpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
			exportedData = this.retrieveResourceFromResponse(lastHttpResponse, ExportedData.class);
		}
	}

	@Then("^the data detail must contains:$")
	public void checkExportedData(DataTable data) throws Exception {
		Map<String, String> detailAsMap = data.asMap(String.class, String.class);
		assertThat(detailAsMap.get("login")).isEqualTo(exportedData.getLogin());
		assertThat(detailAsMap.get("appName")).isEqualTo(exportedData.getAppName());
		assertThat(detailAsMap.get("data")).isEqualTo(exportedData.getData());
	}

}
