package com.yauritux.model.dto;

import com.yauritux.model.constant.Status;

/**
 * @author yauritux@gmail.com
 */
public class StatusResponse {

	private Status status;
	private String message;
	
	public StatusResponse(Status status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public String getMessage() {
		return message;
	}
}
