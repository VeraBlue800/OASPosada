package com.posada.api.resource;
 
import com.posada.api.model.Payment;
import com.posada.api.service.PaymentService;
 
import java.util.List;
 
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
 
@Path("/api/v1") // Prefijo base de la API
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentsResource {
 
    // Inyección de dependencias con CDI
    @Inject
    PaymentService paymentService; // <-- Inyecta el servicio
 
    @POST
    @Path("/payments")
    public Response createPayment(@Valid Payment paymentRequest) {
        System.out.println("Resource - Pago recibido para reserva: " + paymentRequest.getReservationId());
 
        // El service lanza BadRequestException si el método es inválido → mapper devuelve 400
        Payment paymentResponse = paymentService.createPayment(paymentRequest);
 
        System.out.println("Resource - Pago registrado: " + paymentResponse.getAmount() + " via " + paymentResponse.getMethod());
 
        // Devolvemos la respuesta con código 201 (CREATED)
        return Response.status(Response.Status.CREATED).entity(paymentResponse).build();
    }
 
    @GET
    @Path("/payments")
    public Response getPayments() {
        System.out.println("Resource - Obteniendo todos los pagos");
 
        // El resource delega la lógica al service
        List<Payment> payments = paymentService.getAllPayments();
 
        // Devolvemos la lista con código 200 (OK)
        return Response.ok(payments).build();
    }
}
 
