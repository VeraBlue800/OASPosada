package com.ejemplo.api.resource;
 
import java.time.OffsetDateTime;
import java.util.List;
 
import com.ejemplo.api.model.ApiError;
import com.ejemplo.api.model.Reservation;
import com.ejemplo.api.service.ReservationService;
 
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
 
@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {
 
    // Inyección de dependencias con CDI
    @Inject
    ReservationService reservationService;
 
    @POST
    @Path("/reservations")
    public Response createReservation(@Valid Reservation reservationRequest) {
        System.out.println("Resource - Reserva recibida para huésped: " + reservationRequest.getGuestId());
 
        // El resource delega la lógica al service
        Reservation reservationResponse = reservationService.createReservation(reservationRequest);
 
        // Devolvemos la respuesta con código 201 (CREATED)
        return Response.status(Response.Status.CREATED).entity(reservationResponse).build();
    }
 
    @GET
    @Path("/reservations")
    public Response getReservations() {
        System.out.println("Resource - Obteniendo todas las reservas");
 
        // El resource delega la lógica al service
        List<Reservation> reservations = reservationService.getAllReservations();
 
        // Devolvemos la lista con código 200 (OK)
        return Response.ok(reservations).build();
    }
 
    @GET
    @Path("/reservations/{reservationId}")
    public Response getReservationById(@PathParam("reservationId") String reservationId) {
        System.out.println("Resource - Buscando reserva con ID: " + reservationId);
 
        // El resource delega la lógica al service
        Reservation reservationResponse = reservationService.getReservationById(reservationId);
 
        // Si el service devuelve null, la reserva no existe -> 404
        if (reservationResponse == null) {
            ApiError errorResponse = new ApiError();
            errorResponse.setCode("NOT_FOUND");
            errorResponse.setMessage("Reserva con ID " + reservationId + " no encontrada");
            errorResponse.setTimestamp(OffsetDateTime.now());
 
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        }
 
        // Devolvemos la reserva con código 200 (OK)
        return Response.ok(reservationResponse).build();
    }
 
    @PUT
    @Path("/reservations/{reservationId}")
    public Response updateReservation(
            @PathParam("reservationId") String reservationId,
            @Valid Reservation reservationRequest) {
 
        System.out.println("Resource - Actualizando reserva con ID: " + reservationId);
 
        // El resource delega la lógica al service
        Reservation reservationResponse = reservationService.updateReservation(reservationId, reservationRequest);
 
        // Si el service devuelve null, la reserva no existe -> 404
        if (reservationResponse == null) {
            ApiError errorResponse = new ApiError();
            errorResponse.setCode("NOT_FOUND");
            errorResponse.setMessage("Reserva con ID " + reservationId + " no encontrada");
            errorResponse.setTimestamp(OffsetDateTime.now());
 
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        }
 
        // Devolvemos la reserva actualizada con código 200 (OK)
        return Response.ok(reservationResponse).build();
    }
 
    @DELETE
    @Path("/reservations/{reservationId}")
    public Response deleteReservation(@PathParam("reservationId") String reservationId) {
        System.out.println("Resource - Cancelando reserva con ID: " + reservationId);
 
        // El resource delega la lógica al service
        boolean deleted = reservationService.deleteReservation(reservationId);
 
        // Si el service devuelve false, la reserva no existe -> 404
        if (!deleted) {
            ApiError errorResponse = new ApiError();
            errorResponse.setCode("NOT_FOUND");
            errorResponse.setMessage("Reserva con ID " + reservationId + " no encontrada");
            errorResponse.setTimestamp(OffsetDateTime.now());
 
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        }
 
        // Devolvemos 204 (NO CONTENT) — sin body, como indica el OAS
        return Response.noContent().build();
    }
}
 
