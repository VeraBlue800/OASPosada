package com.posada.api.resource;

import com.posada.api.model.ApiError;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.OffsetDateTime;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {

        String messages = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .reduce((a, b) -> a + " | " + b)
                .orElse("Datos inválidos");

        ApiError error = new ApiError();
        error.setCode("VALIDATION_ERROR");
        error.setMessage(messages);
        error.setTimestamp(OffsetDateTime.now());

        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}