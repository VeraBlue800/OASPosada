package com.posada.api.resource;
import com.posada.api.model.Reservation;
import com.posada.api.service.ReservationService;
import java.util.List;
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

@Path("/api/v1") // Prefijo base de la API
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {

    // Inyección de dependencias con CDI
    @Inject
    ReservationService reservationService; // <-- Inyecta el servicio

    @POST
    @Path("/reservations")
    public Response createReservation(@Valid Reservation reservationRequest) {
        System.out.println("Resource - Reserva recibida para huésped: " + reservationRequest.getGuestId());

        // El resource delega la lógica al service
        Reservation reservationResponse = reservationService.createReservation(reservationRequest);

        System.out.println("Resource - Reserva creada para habitación: " + reservationResponse.getRoomId());

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

        // El service lanza NotFoundException si no existe → mapper devuelve 404
        Reservation reservationResponse = reservationService.getReservationById(reservationId);

        return Response.ok(reservationResponse).build();
    }

    @PUT
@Path("/reservations/{reservationId}")
public Response updateReservation(
        @PathParam("reservationId") String reservationId,
        @Valid Reservation reservationRequest) {
    System.out.println("Resource - Actualizando reserva con ID: " + reservationId);

    Reservation reservationResponse = reservationService.updateReservation(reservationId, reservationRequest);

    return Response.ok(reservationResponse).build();
}
    @DELETE
@Path("/reservations/{reservationId}")
public Response deleteReservation(@PathParam("reservationId") String reservationId) {
    System.out.println("Resource - Cancelando reserva con ID: " + reservationId);

    // El service lanza NotFoundException si no existe → mapper devuelve 404
    reservationService.deleteReservation(reservationId);

    return Response.noContent().build();
}
}
