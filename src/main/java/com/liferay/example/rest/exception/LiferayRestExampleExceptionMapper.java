package com.liferay.example.rest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class LiferayRestExampleExceptionMapper  implements ExceptionMapper<LiferayRestExampleException> {

	@Override
	public Response toResponse(LiferayRestExampleException exception) {
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(new ErrorMessage(exception))
				.type(MediaType.APPLICATION_JSON)
				.build();
		
	}

}