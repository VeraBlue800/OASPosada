package com.posada.api.resource;

import com.posada.api.model.Room;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import java.util.List;
import com.posada.api.service.RoomService;
import org.jboss.logging.Logger;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private static final Logger LOG = Logger.getLogger(RoomResource.class);

    @Inject
    RoomService roomService;

    @POST
    @Path("/rooms")
    public Response createRoom(@Valid Room roomRequest) {
        LOG.infof("POST /rooms - Solicitud para crear habitación número: %d", roomRequest.getNumber());
        Room roomResponse = roomService.createRoom(roomRequest);
        LOG.infof("Resource - Room creada: %d", roomResponse.getNumber());
        return Response.status(Response.Status.CREATED)
                .entity("{\"message\": \"Habitación " + roomResponse.getNumber() + " creada\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/rooms")
    public Response getRooms() {
        LOG.info("GET /rooms - Obteniendo lista de rooms");
        List<Room> rooms = roomService.getRooms();
        LOG.infof("Resource - Rooms encontradas: %d", rooms.size());
        return Response.ok(rooms).build();
    }

    @GET
    @Path("/rooms/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        LOG.infof("Resource - Buscando Room con ID: %s", roomId);
        Room roomResponse = roomService.getRoomById(roomId);
        LOG.infof("Resource - Room encontrada: %d", roomResponse.getNumber());
        return Response.ok(roomResponse).build();
    }

    @PUT
    @Path("/rooms/{roomId}")
    public Response updateRoom(@PathParam("roomId") String roomId, @Valid Room roomRequest) {
        LOG.infof("Resource - Actualizando Room con ID: %s", roomId);
        Room roomResponse = roomService.updateRoom(roomId, roomRequest);
        LOG.infof("Resource - Room actualizada: %d", roomResponse.getNumber());
        return Response.ok()
                .entity("{\"message\": \"Habitación " + roomResponse.getNumber() + " actualizada correctamente\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}