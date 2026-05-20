package com.posada.api.mapper;

import com.posada.api.entity.ReservationEntity;
import com.posada.api.model.Reservation;

public class ReservationMapper {

    // Convierte de modelo (API) a Entity (BD)
    public static ReservationEntity toEntity(Reservation reservation) {
        ReservationEntity entity = new ReservationEntity();
        entity.setGuestId(Integer.parseInt(reservation.getGuestId()));
        entity.setRoomNumber(Integer.parseInt(reservation.getRoomId()));
        entity.setCheckIn(reservation.getCheckIn());
        entity.setCheckOut(reservation.getCheckOut());
        return entity;
    }

    // Convierte de Entity (BD) a modelo (API)
    public static Reservation toModel(ReservationEntity entity) {
        Reservation reservation = new Reservation();
        reservation.setGuestId(String.valueOf(entity.getGuestId()));
        reservation.setRoomId(String.valueOf(entity.getRoomNumber()));
        reservation.setCheckIn(entity.getCheckIn());
        reservation.setCheckOut(entity.getCheckOut());
        return reservation;
    }
}