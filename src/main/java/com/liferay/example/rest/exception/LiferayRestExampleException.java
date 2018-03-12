package com.liferay.example.rest.exception;

public class LiferayRestExampleException  extends Exception{
	
private static final long serialVersionUID = -8999932578270387947L;
	
	/** 
	 * Contains redundantly the HTTP status of the response sent back to the client in case of error, so that
	 * the developer does not have to look into the response headers. If null a default 
	 */
	Integer status;
	
	/** detailed error description for developers*/
	String developerMessage;	
	
	/**
	 * 
	 * @param status
	 * @param message
	 * @param developerMessage
	 */
	public LiferayRestExampleException(int status, String message) {
		super(message);
		this.status = status;
	}

	public LiferayRestExampleException() { }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
}
