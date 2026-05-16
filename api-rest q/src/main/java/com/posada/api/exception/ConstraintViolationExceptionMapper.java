package com.posada.api.exception;

import com.posada.api.model.ApiError;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOG = Logger.getLogger(ConstraintViolationExceptionMapper.class);

    @Override
    public Response toResponse(ConstraintViolationException exception) {

        String messages = exception.getConstraintViolations()
                .stream()
                .map(violation -> {
                    String field = violation.getPropertyPath().toString();
                    field = field.contains(".") ? field.substring(field.lastIndexOf('.') + 1) : field;

                    String fieldName = switch (field) {
                        case "number" -> "El número de habitación";
                        case "type"   -> "El tipo de habitación";
                        case "price"  -> "El precio";
                        case "status" -> "El estado";
                        default       -> "El campo '" + field + "'";
                    };

                    String msg = violation.getMessage();
                    String friendlyMsg = switch (msg) {
                        case "must not be null"  -> "es obligatorio";
                        case "must not be blank" -> "no puede estar vacío";
                        default                  -> msg;
                    };

                    return fieldName + " " + friendlyMsg;
                })
                .reduce((a, b) -> a + " | " + b)
                .orElse("Datos inválidos");

        LOG.warnf("400 Validation Error: %s", messages);

        ApiError error = ApiErrorBuilder.build("VALIDATION_ERROR", messages);

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}