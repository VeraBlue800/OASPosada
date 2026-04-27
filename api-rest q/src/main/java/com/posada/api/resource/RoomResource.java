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

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @Inject
    RoomService roomService;

    @POST
    @Path("/rooms")
    public Response createRoom(@Valid Room roomRequest) {
        System.out.println("Resource - Room recibida: " + roomRequest.getNumber());
        Room roomResponse = roomService.createRoom(roomRequest);
        System.out.println("Resource - Room creada: " + roomResponse.getNumber());
        return Response.status(Response.Status.CREATED).entity(roomResponse).build();
    }

    @GET
    @Path("/rooms")
    public Response getRooms() {
        System.out.println("Resource - Obteniendo lista de rooms");
        List<Room> rooms = roomService.getRooms();
        System.out.println("Resource - Rooms encontradas: " + rooms.size());
        return Response.ok(rooms).build();
    }

    @GET
    @Path("/rooms/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        System.out.println("Resource - Buscando Room con ID: " + roomId);
        Room roomResponse = roomService.getRoomById(roomId);
        System.out.println("Resource - Room encontrada: " + roomResponse.getNumber());
        return Response.ok(roomResponse).build();
    }

    @PUT
    @Path("/rooms/{roomId}")
    public Response updateRoom(@PathParam("roomId") String roomId, @Valid Room roomRequest) {
        System.out.println("Resource - Actualizando Room con ID: " + roomId);
        Room roomResponse = roomService.updateRoom(roomId, roomRequest);
        System.out.println("Resource - Room actualizada: " + roomResponse.getNumber());
        return Response.ok(roomResponse).build();
    }
}