package com.posada.api.resource;

import com.posada.api.model.Guest;
import com.posada.api.model.GuestResponse;
import com.posada.api.service.GuestService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GuestResource {

    @Inject
    GuestService guestService;

    // POST /guests — Crear huésped
    @POST
    @Path("/guests")
    public Response createGuest(@Valid Guest guestRequest) {
        System.out.println("Resource - Huésped recibido: " + guestRequest.getName());

        GuestResponse guestResponse = guestService.createGuest(guestRequest);

        return Response.status(Response.Status.CREATED).entity(guestResponse).build();
    }

    // GET /guests — Obtener todos los huéspedes
    @GET
    @Path("/guests")
    public Response getGuests() {
        System.out.println("Resource - Obteniendo lista de huéspedes");

        List<GuestResponse> guests = guestService.getAllGuests();

        return Response.ok(guests).build();
    }

    // GET /guests/{guestId} — Obtener huésped por ID
    @GET
    @Path("/guests/{guestId}")
    public Response getGuestById(@PathParam("guestId") String guestId) {
        System.out.println("Resource - Buscando huésped con ID: " + guestId);

        GuestResponse guestResponse = guestService.getGuestById(guestId);

        if (guestResponse == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(guestResponse).build();
    }

    // PUT /guests/{guestId} — Actualizar huésped
    @PUT
    @Path("/guests/{guestId}")
    public Response updateGuest(@PathParam("guestId") String guestId, @Valid Guest guestRequest) {
        System.out.println("Resource - Actualizando huésped con ID: " + guestId);

        GuestResponse guestResponse = guestService.updateGuest(guestId, guestRequest);

        if (guestResponse == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(guestResponse).build();
    }

    // DELETE /guests/{guestId} — Eliminar huésped
    @DELETE
    @Path("/guests/{guestId}")
    public Response deleteGuest(@PathParam("guestId") String guestId) {
        System.out.println("Resource - Eliminando huésped con ID: " + guestId);

        boolean deleted = guestService.deleteGuest(guestId);

        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.noContent().build(); // 204 No Content
    }
}