package com.posada.api.exception;

import com.posada.api.model.ApiError;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Provider
public class ClientErrorExceptionMapper implements ExceptionMapper<ClientErrorException> {

    private static final Logger LOG = Logger.getLogger(ClientErrorExceptionMapper.class);

    @Override
    public Response toResponse(ClientErrorException exception) {
        LOG.warnf("%d Client Error: %s", exception.getResponse().getStatus(), exception.getMessage());

        ApiError error = ApiErrorBuilder.build(
            "CONFLICT",
            exception.getMessage()
        );

        return Response
                .status(exception.getResponse().getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}