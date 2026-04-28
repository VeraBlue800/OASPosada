package com.ejemplo.api.resource;

import com.ejemplo.api.model.ApiError;
import com.ejemplo.api.model.Reservation;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @POST
    @Path("/reservations")
    public Response createReservation(@Valid Reservation reservationRequest) {
        System.out.println("Resource - Reserva recibida para huésped: " + reservationRequest.getGuestId());

        // Simulamos que la DB persiste la información
        Reservation reservationResponse = new Reservation();
        reservationResponse.setGuestId(reservationRequest.getGuestId());
        reservationResponse.setRoomId(reservationRequest.getRoomId());
        reservationResponse.setCheckIn(reservationRequest.getCheckIn());
        reservationResponse.setCheckOut(reservationRequest.getCheckOut());

        System.out.println("Resource - Reserva creada para habitación: " + reservationResponse.getRoomId());

        // Devolvemos la respuesta con código 201 (CREATED)
        return Response.status(Response.Status.CREATED).entity(reservationResponse).build();
    }

    @GET
    @Path("/reservations")
    public Response getReservations() {
        System.out.println("Resource - Obteniendo todas las reservas");

        // Simulamos buscar las reservas en la base de datos
        List<Reservation> reservations = new ArrayList<>();

        Reservation r1 = new Reservation();
        r1.setGuestId("1");
        r1.setRoomId("101");
        r1.setCheckIn(LocalDate.of(2025, 6, 1));
        r1.setCheckOut(LocalDate.of(2025, 6, 5));
        reservations.add(r1);

        Reservation r2 = new Reservation();
        r2.setGuestId("2");
        r2.setRoomId("102");
        r2.setCheckIn(LocalDate.of(2025, 6, 10));
        r2.setCheckOut(LocalDate.of(2025, 6, 15));
        reservations.add(r2);

        // Devolvemos la lista con código 200 (OK)
        return Response.ok(reservations).build();
    }

    @GET
    @Path("/reservations/{reservationId}")
    public Response getReservationById(@PathParam("reservationId") String reservationId) {
        System.out.println("Resource - Buscando reserva con ID: " + reservationId);

        // Validación básica del ID
        if (reservationId == null || reservationId.isBlank()) {
            ApiError errorResponse = new ApiError();
            errorResponse.setCode("INVALID_INPUT");
            errorResponse.setMessage("El ID de reserva no puede estar vacío");
            errorResponse.setTimestamp(OffsetDateTime.now());

            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        // Simulamos buscar la reserva en la base de datos
        // En un caso real, aquí harías: reservationService.findById(reservationId)
        Reservation reservationResponse = new Reservation();
        reservationResponse.setGuestId("1");
        reservationResponse.setRoomId("101");
        reservationResponse.setCheckIn(LocalDate.of(2025, 6, 1));
        reservationResponse.setCheckOut(LocalDate.of(2025, 6, 5));

        // Devolvemos la reserva con código 200 (OK)
        return Response.ok(reservationResponse).build();
    }

    @PUT
    @Path("/reservations/{reservationId}")
    public Response updateReservation(
            @PathParam("reservationId") String reservationId,
            @Valid Reservation reservationRequest) {

        System.out.println("Resource - Actualizando reserva con ID: " + reservationId);

        // Validación básica del ID
        if (reservationId == null || reservationId.isBlank()) {
            ApiError errorResponse = new ApiError();
            errorResponse.setCode("INVALID_INPUT");
            errorResponse.setMessage("El ID de reserva no puede estar vacío");
            errorResponse.setTimestamp(OffsetDateTime.now());

            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        // Simulamos actualizar la reserva en la base de datos
        // En un caso real, aquí harías: reservationService.update(reservationId, reservationRequest)
        Reservation reservationResponse = new Reservation();
        reservationResponse.setGuestId(reservationRequest.getGuestId());
        reservationResponse.setRoomId(reservationRequest.getRoomId());
        reservationResponse.setCheckIn(reservationRequest.getCheckIn());
        reservationResponse.setCheckOut(reservationRequest.getCheckOut());

        System.out.println("Resource - Reserva actualizada para habitación: " + reservationResponse.getRoomId());

        // Devolvemos la reserva actualizada con código 200 (OK)
        return Response.ok(reservationResponse).build();
    }

    @DELETE
    @Path("/reservations/{reservationId}")
    public Response deleteReservation(@PathParam("reservationId") String reservationId) {
        System.out.println("Resource - Cancelando reserva con ID: " + reservationId);

        // Validación básica del ID
        if (reservationId == null || reservationId.isBlank()) {
            ApiError errorResponse = new ApiError();
            errorResponse.setCode("INVALID_INPUT");
            errorResponse.setMessage("El ID de reserva no puede estar vacío");
            errorResponse.setTimestamp(OffsetDateTime.now());

            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        // Simulamos cancelar la reserva en la base de datos
        // En un caso real, aquí harías: reservationService.delete(reservationId)
        System.out.println("Resource - Reserva cancelada con ID: " + reservationId);

        // Devolvemos 204 (NO CONTENT) — sin body, como indica el OAS
        return Response.noContent().build();
    }
}
