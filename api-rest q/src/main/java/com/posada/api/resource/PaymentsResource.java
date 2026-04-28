package com.ejemplo.api.resource;

import com.ejemplo.api.model.ApiError;
import com.ejemplo.api.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    @POST
    @Path("/payments")
    public Response createPayment(@Valid Payment paymentRequest) {
        System.out.println("Resource - Pago recibido para reserva: " + paymentRequest.getReservationId());

        // Validación del método de pago (el OAS define: card, cash, transfer)
        String method = paymentRequest.getMethod();
        if (!method.equals("card") && !method.equals("cash") && !method.equals("transfer")) {
            ApiError errorResponse = new ApiError();
            errorResponse.setCode("INVALID_INPUT");
            errorResponse.setMessage("Método de pago inválido: " + method + ". Valores permitidos: card, cash, transfer");
            errorResponse.setTimestamp(OffsetDateTime.now());

            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        // Simulamos que la DB persiste la información
        Payment paymentResponse = new Payment();
        paymentResponse.setReservationId(paymentRequest.getReservationId());
        paymentResponse.setAmount(paymentRequest.getAmount());
        paymentResponse.setMethod(paymentRequest.getMethod());
        paymentResponse.setDate(paymentRequest.getDate());

        System.out.println("Resource - Pago registrado: " + paymentResponse.getAmount() + " via " + paymentResponse.getMethod());

        // Devolvemos la respuesta con código 201 (CREATED)
        return Response.status(Response.Status.CREATED).entity(paymentResponse).build();
    }

    @GET
    @Path("/payments")
    public Response getPayments() {
        System.out.println("Resource - Obteniendo todos los pagos");

        // Simulamos buscar los pagos en la base de datos
        // En un caso real, aquí harías: paymentService.findAll()
        List<Payment> payments = new ArrayList<>();

        Payment p1 = new Payment();
        p1.setReservationId("1");
        p1.setAmount(new BigDecimal("1200.00"));
        p1.setMethod("cash");
        p1.setDate(LocalDate.of(2025, 6, 1));
        payments.add(p1);

        Payment p2 = new Payment();
        p2.setReservationId("2");
        p2.setAmount(new BigDecimal("2400.00"));
        p2.setMethod("card");
        p2.setDate(LocalDate.of(2025, 6, 10));
        payments.add(p2);

        // Devolvemos la lista con código 200 (OK)
        return Response.ok(payments).build();
    }
}
