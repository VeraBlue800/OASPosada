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
import org.jboss.logging.Logger;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {

    private static final Logger LOG = Logger.getLogger(ReservationResource.class);

    @Inject
    ReservationService reservationService;

    @POST
    @Path("/reservations")
    public Response createReservation(@Valid Reservation reservationRequest) {
        LOG.infof("POST /reservations - Solicitud para crear reserva para huésped: %s",
                reservationRequest.getGuestId());
        Reservation reservationResponse = reservationService.createReservation(reservationRequest);
        LOG.infof("POST /reservations - Reserva creada para habitación: %s", reservationResponse.getRoomId());
        return Response.status(Response.Status.CREATED)
                .entity("{\"message\": \"Reserva para habitación " + reservationResponse.getRoomId() + " creada\"}")
                .type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                .build();
    }

    @GET
    @Path("/reservations")
    public Response getReservations() {
        LOG.info("GET /reservations - Solicitud para obtener todas las reservas");
        List<Reservation> reservations = reservationService.getAllReservations();
        LOG.infof("GET /reservations - Se encontraron %d reservas", reservations.size());
        return Response.ok(reservations).build();
    }

    @GET
    @Path("/reservations/{reservationId}")
    public Response getReservationById(@PathParam("reservationId") String reservationId) {
        LOG.infof("GET /reservations/%s - Solicitud para obtener reserva", reservationId);
        Reservation reservationResponse = reservationService.getReservationById(reservationId);
        LOG.infof("GET /reservations/%s - Reserva encontrada", reservationId);
        return Response.ok(reservationResponse).build();
    }

    @PUT
    @Path("/reservations/{reservationId}")
    public Response updateReservation(
            @PathParam("reservationId") String reservationId,
            @Valid Reservation reservationRequest) {
        LOG.infof("PUT /reservations/%s - Solicitud para actualizar reserva", reservationId);
        Reservation reservationResponse = reservationService.updateReservation(reservationId, reservationRequest);
        LOG.infof("PUT /reservations/%s - Reserva actualizada correctamente", reservationId);
        return Response.ok()
                .entity("{\"message\": \"Reserva " + reservationId + " actualizada correctamente\"}")
                .type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                .build();
    }

    @DELETE
    @Path("/reservations/{reservationId}")
    public Response deleteReservation(@PathParam("reservationId") String reservationId) {
        LOG.infof("DELETE /reservations/%s - Solicitud para cancelar reserva", reservationId);
        reservationService.deleteReservation(reservationId);
        LOG.infof("DELETE /reservations/%s - Reserva cancelada correctamente", reservationId);
        return Response.ok()
                .entity("{\"message\": \"Reserva " + reservationId + " cancelada correctamente\"}")
                .type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                .build();
    }
}