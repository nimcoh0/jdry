package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MyJsonProcessingExceptionHandler implements ExceptionMapper<JsonProcessingException> {

    @Override
    public Response toResponse(JsonProcessingException exception) {

        //log.severe(exception.getMessage());

        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();

    }
}
