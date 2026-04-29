package com.posada.api.resource;

import com.posada.api.model.ApiError;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.OffsetDateTime;

@Provider
public class JsonExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    @Override
    public Response toResponse(JsonProcessingException exception) {

        ApiError error = new ApiError();
        error.setCode("INVALID_JSON");
        error.setMessage("El cuerpo de la solicitud contiene un valor inválido: " + exception.getOriginalMessage());
        error.setTimestamp(OffsetDateTime.now());

        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
//HOLA