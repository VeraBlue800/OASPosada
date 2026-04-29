//Prueba
package com.posada.api.resource;

import com.posada.api.model.ApiError;
import com.posada.api.model.Guest;
import com.posada.api.model.GuestResponse;
import com.posada.api.service.GuestService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.OffsetDateTime;
import java.util.List;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@jakarta.validation.executable.ValidateOnExecution
public class GuestResource {

    @Inject
    GuestService guestService;

    // Método auxiliar para construir un ApiError 404 con mensaje personalizado
    private Response notFound(String guestId) {
        ApiError error = new ApiError();
        error.setCode("GUEST_NOT_FOUND");
        error.setMessage("No se encontró ningún huésped con el ID: " + guestId);
        error.setTimestamp(OffsetDateTime.now());
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }

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
    public Response getGuestById(
            @Pattern(regexp = "^[0-9]+$", message = "El guestId debe ser un número")
            @PathParam("guestId") String guestId) {
        System.out.println("Resource - Buscando huésped con ID: " + guestId);

        GuestResponse guestResponse = guestService.getGuestById(guestId);

        if (guestResponse == null) {
            return notFound(guestId);
        }

        return Response.ok(guestResponse).build();
    }

    // PUT /guests/{guestId} — Actualizar huésped
    @PUT
    @Path("/guests/{guestId}")
    public Response updateGuest(
            @Pattern(regexp = "^[0-9]+$", message = "El guestId debe ser un número")
            @PathParam("guestId") String guestId, @Valid Guest guestRequest) {
        System.out.println("Resource - Actualizando huésped con ID: " + guestId);

        GuestResponse guestResponse = guestService.updateGuest(guestId, guestRequest);

        if (guestResponse == null) {
            return notFound(guestId);
        }

        return Response.ok(guestResponse).build();
    }

    // DELETE /guests/{guestId} — Eliminar huésped
    @DELETE
    @Path("/guests/{guestId}")
    public Response deleteGuest(
            @Pattern(regexp = "^[0-9]+$", message = "El guestId debe ser un número")
            @PathParam("guestId") String guestId) {
        System.out.println("Resource - Eliminando huésped con ID: " + guestId);

        boolean deleted = guestService.deleteGuest(guestId);

        if (!deleted) {
            return notFound(guestId);
        }

        return Response.noContent().build(); // 204 No Content
    }
}
