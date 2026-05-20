package com.posada.api.mapper;

import com.posada.api.entity.RoomEntity;
import com.posada.api.model.Room;

public class RoomMapper {

    // Convierte de modelo (API) a Entity (BD)
    public static RoomEntity toEntity(Room room) {
        RoomEntity entity = new RoomEntity();
        entity.setNumber(room.getNumber());
        entity.setType(RoomEntity.Type.valueOf(
            room.getType().getValue().replace(" ", "_")));
        entity.setPrice(room.getPrice());
        entity.setStatus(RoomEntity.Status.valueOf(
            room.getStatus().getValue().replace(" ", "_")));
        return entity;
    }

    // Convierte de Entity (BD) a modelo (API)
    public static Room toModel(RoomEntity entity) {
        Room room = new Room();
        room.setNumber(entity.getNumber());
        room.setType(Room.TypeEnum.fromValue(
            entity.getType().name().replace("_", " ")));
        room.setPrice(entity.getPrice());
        room.setStatus(Room.StatusEnum.fromValue(
            entity.getStatus().name().replace("_", " ")));
        return room;
    }
}