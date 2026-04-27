package com.posada.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import com.posada.api.model.Room;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoomService {

    public Room createRoom(Room room) {
        System.out.println("Service - Room recibida: " + room.getNumber());

        // Simulamos la persistencia y generación de datos del sistema
        Room response = new Room();

        // Mapeo de campos desde el Request
        response.setNumber(room.getNumber());
        response.setType(room.getType());
        response.setPrice(room.getPrice());

        // Dato generado por el servidor: status por defecto al crear
        response.setStatus(Room.StatusEnum.DISPONIBLE);

        System.out.println("Service - Room creada: " + response.getNumber());

        return response;
    }

    public List<Room> getRooms() {
        System.out.println("Service - Obteniendo lista de rooms");

        // Simulación de búsqueda en base de datos
        List<Room> rooms = new ArrayList<>();

        Room room1 = new Room();
        room1.setNumber(101);
        room1.setType(Room.TypeEnum.SENCILLA);
        room1.setPrice(new BigDecimal("800.0"));
        room1.setStatus(Room.StatusEnum.DISPONIBLE);

        Room room2 = new Room();
        room2.setNumber(102);
        room2.setType(Room.TypeEnum.DOBLE);
        room2.setPrice(new BigDecimal("1200.0"));
        room2.setStatus(Room.StatusEnum.OCUPADA);

        rooms.add(room1);
        rooms.add(room2);

        System.out.println("Service - Rooms encontradas: " + rooms.size());

        return rooms;
    }

    public Room getRoomById(String roomId) {
        // Validar que el ID sea numérico
        if (roomId == null || !roomId.matches("\\d+")) {
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido: " + roomId);
        }

        Room response = new Room();
        response.setNumber(Integer.parseInt(roomId));
        response.setType(Room.TypeEnum.SENCILLA);
        response.setPrice(new BigDecimal("800.0"));
        response.setStatus(Room.StatusEnum.DISPONIBLE);

        return response;
    }

    public Room updateRoom(String roomId, Room room) {
        if (roomId == null || !roomId.matches("\\d+")) {
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido: " + roomId);
        }

        Room response = new Room();
        response.setNumber(room.getNumber());
        response.setType(room.getType());
        response.setPrice(room.getPrice());
        response.setStatus(room.getStatus());

        return response;
    }
}