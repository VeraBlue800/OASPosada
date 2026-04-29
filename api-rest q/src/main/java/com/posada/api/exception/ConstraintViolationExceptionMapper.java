package com.posada.api.exception;

import com.posada.api.model.ApiError;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        ApiError error = ApiErrorBuilder.build("BAD_REQUEST", exception.getMessage());

        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}