package com.posada.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import com.posada.api.model.Room;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RoomService {

    private static final Logger LOG = Logger.getLogger(RoomService.class);


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
        LOG.infof("Service - Intentando crear habitación número: %d", room.getNumber());

        boolean exists = rooms.stream()
                .anyMatch(r -> r.getNumber().equals(room.getNumber()));
        if (exists) {
            LOG.warnf("Service - Conflicto: ya existe la habitación número %d", room.getNumber());
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

        LOG.infof("Service - Room creada: %d", response.getNumber());

        return response;
    }

    public List<Room> getRooms() {
        LOG.infof("Service - Obteniendo lista de habitaciones, total: %d", rooms.size());
        return new ArrayList<>(rooms);
    }

    public Room getRoomById(String roomId) {
        LOG.infof("Service - Buscando habitación con ID: %s", roomId);
        if (roomId == null || !roomId.matches("\\d+")) {
            LOG.warnf("Service - ID inválido recibido: %s", roomId);
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido");
        }

        int id = Integer.parseInt(roomId);

        return rooms.stream()
                .filter(r -> r.getNumber().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    LOG.warnf("Service - Habitación no encontrada con número: %d", id);
                    return new jakarta.ws.rs.NotFoundException("Habitación no encontrada con el número: " + roomId);
                });
    }

    public Room updateRoom(String roomId, Room room) {
        LOG.infof("Service - Actualizando habitación con ID: %s", roomId);
        if (roomId == null || !roomId.matches("\\d+")) {
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido: " + roomId);
        }

        int id = Integer.parseInt(roomId);

        Room existing = rooms.stream()
                .filter(r -> r.getNumber().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    LOG.warnf("Service - Habitación no encontrada con número: %d", id);
                    return new jakarta.ws.rs.NotFoundException("Habitación no encontrada con el número: " + roomId);
                });

        existing.setType(room.getType());
        existing.setPrice(room.getPrice());
        existing.setStatus(room.getStatus());

        LOG.infof("Service - Habitación %d actualizada exitosamente", existing.getNumber());

        return existing;
    }
}