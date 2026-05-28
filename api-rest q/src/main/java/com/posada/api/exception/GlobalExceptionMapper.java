package com.posada.api.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
//error general
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Exception e) {

        // Excepciones JAX-RS controladas (404, 400, 409...)
        if (e instanceof WebApplicationException wae) {
            String message = wae.getMessage() != null ? wae.getMessage() : "Error en la solicitud";
            return Response.status(wae.getResponse().getStatus())
                    .entity("{\"error\": \"" + message + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // Cualquier otro error inesperado
        LOG.errorf(e, "Error inesperado: %s", e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error interno del servidor\", \"detalle\": \"" + e.getMessage() + "\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}