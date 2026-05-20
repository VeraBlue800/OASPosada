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
import org.jboss.logging.Logger;

import java.time.OffsetDateTime;
import java.util.List;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@jakarta.validation.executable.ValidateOnExecution
public class GuestResource {

    private static final Logger LOG = Logger.getLogger(GuestResource.class);

    @Inject
    GuestService guestService;

    private Response notFound(String guestId) {
        ApiError error = new ApiError();
        error.setCode("GUEST_NOT_FOUND");
        error.setMessage("No se encontró ningún huésped con el ID: " + guestId);
        error.setTimestamp(OffsetDateTime.now());
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }

    @POST
    @Path("/guests")
    public Response createGuest(@Valid Guest guestRequest) {
        LOG.infof("POST /guests - Solicitud para crear huésped: %s", guestRequest.getName());

        GuestResponse guestResponse = guestService.createGuest(guestRequest);

        LOG.infof("Resource - Huésped creado con ID: %s", guestResponse.getId());
        return Response.status(Response.Status.CREATED).entity(guestResponse).build();
    }

    @GET
    @Path("/guests")
    public Response getGuests() {
        LOG.info("GET /guests - Obteniendo lista de huéspedes");

        List<GuestResponse> guests = guestService.getAllGuests();

        LOG.infof("Resource - Huéspedes encontrados: %d", guests.size());
        return Response.ok(guests).build();
    }

    @GET
    @Path("/guests/{guestId}")
    public Response getGuestById(
            @Pattern(regexp = "^[0-9]+$", message = "El guestId debe ser un número")
            @PathParam("guestId") String guestId) {
        LOG.infof("GET /guests/%s - Buscando huésped", guestId);

        GuestResponse guestResponse = guestService.getGuestById(guestId);

        if (guestResponse == null) {
            LOG.warnf("Resource - Huésped no encontrado con ID: %s", guestId);
            return notFound(guestId);
        }

        LOG.infof("Resource - Huésped encontrado: %s", guestResponse.getName());
        return Response.ok(guestResponse).build();
    }

    @PUT
    @Path("/guests/{guestId}")
    public Response updateGuest(
            @Pattern(regexp = "^[0-9]+$", message = "El guestId debe ser un número")
            @PathParam("guestId") String guestId, @Valid Guest guestRequest) {
        LOG.infof("PUT /guests/%s - Actualizando huésped", guestId);

        GuestResponse guestResponse = guestService.updateGuest(guestId, guestRequest);

        if (guestResponse == null) {
            LOG.warnf("Resource - Huésped no encontrado con ID: %s", guestId);
            return notFound(guestId);
        }

        LOG.infof("Resource - Huésped actualizado: %s", guestResponse.getName());
        return Response.ok(guestResponse).build();
    }

    @DELETE
    @Path("/guests/{guestId}")
    public Response deleteGuest(
            @Pattern(regexp = "^[0-9]+$", message = "El guestId debe ser un número")
            @PathParam("guestId") String guestId) {
        LOG.infof("DELETE /guests/%s - Eliminando huésped", guestId);

        boolean deleted = guestService.deleteGuest(guestId);

        if (!deleted) {
            LOG.warnf("Resource - Huésped no encontrado con ID: %s", guestId);
            return notFound(guestId);
        }

        LOG.infof("Resource - Huésped eliminado con ID: %s", guestId);
        return Response.noContent().build();
    }
}