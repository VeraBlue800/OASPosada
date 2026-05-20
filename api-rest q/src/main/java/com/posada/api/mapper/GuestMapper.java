package com.posada.api.mapper;

import com.posada.api.entity.GuestEntity;
import com.posada.api.model.Guest;
import com.posada.api.model.GuestResponse;

public class GuestMapper {

    // Convierte de modelo (API) a Entity (BD)
    public static GuestEntity toEntity(Guest guest) {
        GuestEntity entity = new GuestEntity();
        entity.setName(guest.getName());
        entity.setPhone(guest.getPhone());
        entity.setEmail(guest.getEmail());
        return entity;
    }

    // Convierte de Entity (BD) a modelo (API)
    public static GuestResponse toModel(GuestEntity entity) {
        GuestResponse response = new GuestResponse();
        response.setId(String.valueOf(entity.getId()));
        response.setName(entity.getName());
        response.setPhone(entity.getPhone());
        response.setEmail(entity.getEmail());
        return response;
    }
}