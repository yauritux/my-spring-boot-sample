package com.yauritux.model.dto;

/**
 * @author yauritux@gmail.com
 */
public class UserSessionRequest {

	private String username;
	private String password;
	
	public UserSessionRequest() {
		super();
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
