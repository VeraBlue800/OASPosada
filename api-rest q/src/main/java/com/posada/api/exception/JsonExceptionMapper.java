package com.posada.api.exception;

import com.posada.api.model.ApiError;

import org.jboss.logging.Logger;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.OffsetDateTime;

@Provider
public class JsonExceptionMapper implements ExceptionMapper<WebApplicationException> {

    private static final Logger LOG = Logger.getLogger(JsonExceptionMapper.class);

    @Override
    public Response toResponse(WebApplicationException exception) {

        if (exception.getResponse().getStatus() != 400) {
            return exception.getResponse();
        }

        LOG.warnf("400 Invalid JSON: %s", exception.getMessage());

        ApiError error = ApiErrorBuilder.build(
                "INVALID_JSON",
                "El cuerpo de la solicitud contiene un formato inválido. Verifica que los datos estén correctamente escritos.");

        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}