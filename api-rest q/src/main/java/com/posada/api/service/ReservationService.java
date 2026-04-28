package com.posada.api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.posada.api.model.Reservation;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReservationService {

    public Reservation createReservation(Reservation reservation) {
        System.out.println("Service - Reserva recibida para huésped: " + reservation.getGuestId());

        // Simulamos la persistencia y generación de datos del sistema
        Reservation response = new Reservation();

        // Mapeo de campos desde el Request
        response.setGuestId(reservation.getGuestId());
        response.setRoomId(reservation.getRoomId());
        response.setCheckIn(reservation.getCheckIn());
        response.setCheckOut(reservation.getCheckOut());

        // Dato generado por el servidor
        response.setId(String.valueOf(System.currentTimeMillis()));

        System.out.println("Service - Reserva creada con ID: " + response.getId());
        return response;
    }

    public List<Reservation> getAllReservations() {
        System.out.println("Service - Obteniendo todas las reservas");

        // Simulación de consulta a base de datos
        List<Reservation> reservations = new ArrayList<>();

        Reservation r1 = new Reservation();
        r1.setId("1");
        r1.setGuestId("1");
        r1.setRoomId("101");
        r1.setCheckIn(LocalDate.of(2025, 6, 1));
        r1.setCheckOut(LocalDate.of(2025, 6, 5));
        reservations.add(r1);

        Reservation r2 = new Reservation();
        r2.setId("2");
        r2.setGuestId("2");
        r2.setRoomId("102");
        r2.setCheckIn(LocalDate.of(2025, 6, 10));
        r2.setCheckOut(LocalDate.of(2025, 6, 15));
        reservations.add(r2);

        return reservations;
    }

    public Reservation getReservationById(String reservationId) {
        System.out.println("Service - Buscando reserva con ID: " + reservationId);

        // Simulación de búsqueda en base de datos
        Reservation response = new Reservation();
        response.setId(reservationId);
        response.setGuestId("1");
        response.setRoomId("101");
        response.setCheckIn(LocalDate.of(2025, 6, 1));
        response.setCheckOut(LocalDate.of(2025, 6, 5));

        return response;
    }

    public Reservation updateReservation(String reservationId, Reservation reservation) {
        System.out.println("Service - Actualizando reserva con ID: " + reservationId);

        // Simulamos la actualización
        Reservation response = new Reservation();
        response.setId(reservationId);
        response.setGuestId(reservation.getGuestId());
        response.setRoomId(reservation.getRoomId());
        response.setCheckIn(reservation.getCheckIn());
        response.setCheckOut(reservation.getCheckOut());

        System.out.println("Service - Reserva actualizada: " + response.getId());
        return response;
    }

    public void deleteReservation(String reservationId) {
        System.out.println("Service - Cancelando reserva con ID: " + reservationId);
        // Simulamos la eliminación (en real: repositorio.deleteById(reservationId))
    }
}
