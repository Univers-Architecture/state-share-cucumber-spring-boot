package com.univers.architecture.cucumber.step;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Steps base class containing utility methods only
 * 
 * @author Issam SABIR
 *
 */
public abstract class AbstractBaseStepDefinition {

	private final Logger log = LoggerFactory.getLogger(AbstractBaseStepDefinition.class);

	/**
	 * Retrieve resource from response.
	 *
	 * @param <T>
	 *            the generic type
	 * @param response
	 *            the response
	 * @param clazz
	 *            the clazz
	 * @return the t
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public <T> T retrieveResourceFromResponse(HttpResponse response, Class<T> clazz) throws IOException {

		log.info("Retreiving value for class {}", clazz);

		String jsonFromResponse = EntityUtils.toString(response.getEntity());
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(jsonFromResponse, clazz);
	}

}
