package com.ragku.rest;

public class RestException extends RuntimeException{

	private static final long serialVersionUID = -3763915126678516210L;
	
	private int httpStatus;
	
	public RestException(int httpStatus) {
		super();
		this.httpStatus = httpStatus;
	}
	
	public RestException(int httpStatus, String msg) {
		super(msg);
		this.httpStatus = httpStatus;
	}

	public int getHttpStatus() {
		return httpStatus;
	}
}
