package com.posada.api.exception;

import com.posada.api.model.ApiError;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        ApiError error = ApiErrorBuilder.build("NOT_FOUND", exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}