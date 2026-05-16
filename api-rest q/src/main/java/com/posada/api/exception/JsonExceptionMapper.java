package com.posada.api.exception;

import com.posada.api.model.ApiError;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.OffsetDateTime;

@Provider
public class JsonExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException exception) {

        System.out.println("Excepción capturada: " + exception.getClass().getName());
        System.out.println("Mensaje: " + exception.getMessage());

        ApiError error = ApiErrorBuilder.build(
                "INVALID_JSON",
                "El cuerpo de la solicitud contiene un formato inválido. Verifica que los datos estén correctamente escritos.");
                error.setTimestamp(OffsetDateTime.now());

        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}