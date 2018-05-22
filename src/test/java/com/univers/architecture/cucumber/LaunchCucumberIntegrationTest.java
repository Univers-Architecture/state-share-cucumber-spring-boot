package com.univers.architecture.cucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import net.minidev.json.JSONObject;

/**
 * 
 * Entry point for Junit/Cucumber tests.
 * 
 * @author Issam SABIR
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", plugin = { "pretty", "html:target/cucumber",
		"json:target/cucumber.json" }, monochrome = true)
public class LaunchCucumberIntegrationTest {

	private static final Logger log = LoggerFactory.getLogger(LaunchCucumberIntegrationTest.class);
	private static WireMockServer wireMockServer;

	@BeforeClass
	public static void initWireMockServer() {

		log.info("Start Init Wire Mock Server ");

		wireMockServer = new WireMockServer();
		wireMockServer.start();

		// Mock OK call to : http://localhost:8080/appname/api/authenticate
		Map<String, String> token = new HashedMap<>();
		token.put("expires", "12345678");
		String tokenValue = "morocco-token";
		token.put("token", tokenValue);
		stubFor(post(urlEqualTo("/appname/api/authenticate"))
				.withRequestBody(equalTo("username=appUser&password=appPass"))
				.willReturn(aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", APPLICATION_JSON_UTF8_VALUE)
						.withBody(new JSONObject(token).toJSONString())));

		// Mock OK call to : http://localhost:8080/appname/api/authenticate
		Map<String, String> token2 = new HashedMap<>();
		token2.put("expires", "123485278");
		String tokenValue2 = "test2-morocco-token";
		token2.put("token", tokenValue2);
		stubFor(post(urlEqualTo("/appname/api/authenticate"))
				.withRequestBody(equalTo("username=appUserABC&password=appPassABC"))
				.willReturn(aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", APPLICATION_JSON_UTF8_VALUE)
						.withBody(new JSONObject(token2).toJSONString())));

		// Mock OK call to : http://localhost:8080/appname/api/users/current
		// --> With token
		Map<String, String> user = new HashedMap<>();
		user.put("login", "appUser");
		user.put("firstName", "user");
		user.put("email", "user@ua-morocco.com");
		user.put("house", "Africa");
		user.put("activated", "true");

		stubFor(get(urlEqualTo("/appname/api/users/current")).withHeader("x-auth-token", equalTo(tokenValue))
				.willReturn(aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", APPLICATION_JSON_UTF8_VALUE)
						.withBody(new JSONObject(user).toJSONString())));

		// Mock OK call to : http://localhost:8080/appname/api/data/export
		// --> With token
		Map<String, String> exportedData = new HashedMap<>();
		exportedData.put("login", "appUser");
		exportedData.put("appName", "appname");
		exportedData.put("data", "examples of data");

		stubFor(get(urlEqualTo("/appname/api/data/export")).withHeader("x-auth-token", equalTo(tokenValue2))
				.willReturn(aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", APPLICATION_JSON_UTF8_VALUE)
						.withBody(new JSONObject(exportedData).toJSONString())));

		log.info("End Init Wire Mock Server ");
	}

	@AfterClass
	public static void stopWireMockServer() {
		log.info("Start Stopping Wire Server ");
		WireMock.reset();
		wireMockServer.stop();
		log.info("End Stopping Wire Server ");
	}
}
