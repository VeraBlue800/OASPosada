package com.posada.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import com.posada.api.model.Room;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class RoomService {

    private final List<Room> rooms = new ArrayList<>(List.of(
            crearRoom(101, Room.TypeEnum.SENCILLA, new BigDecimal("800.0"), Room.StatusEnum.DISPONIBLE),
            crearRoom(102, Room.TypeEnum.DOBLE, new BigDecimal("1200.0"), Room.StatusEnum.OCUPADA)));

    private Room crearRoom(int number, Room.TypeEnum type, BigDecimal price, Room.StatusEnum status) {
        Room r = new Room();
        r.setNumber(number);
        r.setType(type);
        r.setPrice(price);
        r.setStatus(status);
        return r;
    }

    public Room createRoom(Room room) {
        System.out.println("Service - Room recibida: " + room.getNumber());

        boolean exists = rooms.stream()
                .anyMatch(r -> r.getNumber().equals(room.getNumber()));
        if (exists) {
            throw new jakarta.ws.rs.ClientErrorException(
                    "Ya existe una habitación con el número " + room.getNumber(),
                    Response.Status.CONFLICT);
        }

        Room response = new Room();

        response.setNumber(room.getNumber());
        response.setType(room.getType());
        response.setPrice(room.getPrice());

        response.setStatus(Room.StatusEnum.DISPONIBLE);

        rooms.add(response);

        System.out.println("Service - Room creada: " + response.getNumber());

        return response;
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public Room getRoomById(String roomId) {
        if (roomId == null || !roomId.matches("\\d+")) {
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido");
        }

        int id = Integer.parseInt(roomId);

        return rooms.stream()
                .filter(r -> r.getNumber().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new jakarta.ws.rs.NotFoundException("Habitación no encontrada con el número: " + roomId));
    }

    public Room updateRoom(String roomId, Room room) {
        if (roomId == null || !roomId.matches("\\d+")) {
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido: " + roomId);
        }

        int id = Integer.parseInt(roomId);

        Room existing = rooms.stream()
                .filter(r -> r.getNumber().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new jakarta.ws.rs.NotFoundException("Habitación no encontrada con el número: " + roomId));

        existing.setType(room.getType());
        existing.setPrice(room.getPrice());
        existing.setStatus(room.getStatus());

        return existing;
    }
}
