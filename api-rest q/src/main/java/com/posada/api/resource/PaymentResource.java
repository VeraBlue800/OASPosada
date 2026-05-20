package com.posada.api.resource;

import com.posada.api.model.ApiError;
import com.posada.api.model.Payment;
import com.posada.api.service.PaymentService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@jakarta.validation.executable.ValidateOnExecution
public class PaymentResource {

    private static final Logger LOG = Logger.getLogger(PaymentResource.class);

    @Inject
    PaymentService paymentService;

    private Response paymentRejected(String code, String message) {
        ApiError error = new ApiError();
        error.setCode(code);
        error.setMessage(message);
        error.setTimestamp(OffsetDateTime.now());
        return Response.status(422).entity(error).build();
    }

    private Response badRequest(String code, String message) {
        ApiError error = new ApiError();
        error.setCode(code);
        error.setMessage(message);
        error.setTimestamp(OffsetDateTime.now());
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }

    @POST
    @Path("/payments")
    public Response createPayment(@Valid Payment paymentRequest) {
        LOG.infof("POST /payments - Solicitud para registrar pago de reserva: %s", paymentRequest.getReservationId());

        // Validación: reservationId no puede ser nulo ni vacío
        if (paymentRequest.getReservationId() == null || paymentRequest.getReservationId().isBlank()) {
            LOG.warn("Resource - reservationId vacío o nulo");
            return badRequest("VALIDATION_ERROR", "El reservationId no puede estar vacío");
        }

        // Validación: reservationId solo números
        if (!paymentRequest.getReservationId().matches("^[0-9]+$")) {
            LOG.warnf("Resource - reservationId inválido: %s", paymentRequest.getReservationId());
            return badRequest("VALIDATION_ERROR", "El reservationId solo puede contener números");
        }

        // Validación: amount obligatorio y mayor que 0
        if (paymentRequest.getAmount() == null) {
            LOG.warn("Resource - amount nulo");
            return badRequest("VALIDATION_ERROR", "El monto (amount) es obligatorio");
        }
        if (paymentRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            LOG.warnf("Resource - amount inválido: %s", paymentRequest.getAmount());
            return badRequest("VALIDATION_ERROR", "El monto debe ser mayor a 0");
        }

        // Validación: method obligatorio
        if (paymentRequest.getMethod() == null) {
            LOG.warn("Resource - method nulo");
            return badRequest("VALIDATION_ERROR", "El método de pago es obligatorio (card, cash, transfer)");
        }

        // Validación: date obligatoria
        if (paymentRequest.getDate() == null) {
            LOG.warn("Resource - date nula");
            return badRequest("VALIDATION_ERROR", "La fecha del pago es obligatoria");
        }

        // Validación de negocio: la fecha no puede ser futura — 422
        if (paymentRequest.getDate().isAfter(LocalDate.now())) {
            LOG.warnf("Resource - Fecha futura rechazada: %s", paymentRequest.getDate());
            return paymentRejected("PAYMENT_REJECTED", "La fecha del pago no puede ser futura: " + paymentRequest.getDate());
        }

        // Validación de negocio: no puede haber dos pagos para la misma reserva — 422
        if (paymentService.existsByReservationId(paymentRequest.getReservationId())) {
            LOG.warnf("Resource - Pago duplicado para reserva: %s", paymentRequest.getReservationId());
            return paymentRejected("DUPLICATE_PAYMENT", "Ya existe un pago registrado para la reserva: " + paymentRequest.getReservationId());
        }

        Payment payment = paymentService.createPayment(paymentRequest);

        LOG.infof("Resource - Pago registrado: %s via %s", payment.getAmount(), payment.getMethod());
        return Response.status(Response.Status.CREATED).entity(payment).build();
    }

    @GET
    @Path("/payments")
    public Response getPayments() {
        LOG.info("GET /payments - Obteniendo lista de pagos");

        List<Payment> payments = paymentService.getAllPayments();

        LOG.infof("Resource - Pagos encontrados: %d", payments.size());
        return Response.ok(payments).build();
    }
}