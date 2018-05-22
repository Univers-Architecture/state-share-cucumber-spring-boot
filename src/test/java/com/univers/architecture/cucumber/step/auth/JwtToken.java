package com.univers.architecture.cucumber.step.auth;

/**
 * The Class JwtToken.
 * 
 * JWT token infos
 * 
 * @author Issam SABIR
 */
public class JwtToken {

	private String token;
	private long expires;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

}
