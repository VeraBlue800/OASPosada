package com.posada.api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ejemplo.api.model.Reservation;

import jakarta.enterprise.context.ApplicationScoped; // <-- Importante

@ApplicationScoped // Le dice a Quarkus que gestione esta clase como un servicio
public class ReservationService {

    private static final List<String> reservasExistentes = List.of("1", "2");

    public Reservation createReservation(Reservation reservation) {
        System.out.println("Service - Reserva recibida para huésped: " + reservation.getGuestId());

        // Simulamos la persistencia y generación de datos del sistema
        Reservation response = new Reservation();

        // Mapeo de campos desde el Request
        response.setGuestId(reservation.getGuestId());
        response.setRoomId(reservation.getRoomId());
        response.setCheckIn(reservation.getCheckIn());
        response.setCheckOut(reservation.getCheckOut());
        // NOTA: El modelo Reservation no tiene campo 'id' (no está en el OAS schema)

        return response;
    }

    public List<Reservation> getAllReservations() {
        System.out.println("Service - Obteniendo todas las reservas");

        // Simulación de búsqueda en base de datos
        List<Reservation> reservations = new ArrayList<>();

        Reservation r1 = new Reservation();
        r1.setGuestId("1");
        r1.setRoomId("101");
        r1.setCheckIn(LocalDate.of(2025, 6, 1));
        r1.setCheckOut(LocalDate.of(2025, 6, 5));
        reservations.add(r1);

        Reservation r2 = new Reservation();
        r2.setGuestId("2");
        r2.setRoomId("102");
        r2.setCheckIn(LocalDate.of(2025, 6, 10));
        r2.setCheckOut(LocalDate.of(2025, 6, 15));
        reservations.add(r2);

        return reservations;
    }

    public Reservation getReservationById(String reservationId) {
        System.out.println("Service - Buscando reserva con ID: " + reservationId);

        if (!reservasExistentes.contains(reservationId)) {
            throw new jakarta.ws.rs.NotFoundException(
                    "Reserva no encontrada con ID: " + reservationId);
        }

        Reservation response = new Reservation();
        response.setGuestId("1");
        response.setRoomId("101");
        response.setCheckIn(LocalDate.of(2025, 6, 1));
        response.setCheckOut(LocalDate.of(2025, 6, 5));
        return response;
    }

    public Reservation updateReservation(String reservationId, Reservation reservation) {
        System.out.println("Service - Actualizando reserva con ID: " + reservationId);

        if (!reservasExistentes.contains(reservationId)) {
            throw new jakarta.ws.rs.NotFoundException(
                    "Reserva no encontrada con ID: " + reservationId);
        }

        Reservation response = new Reservation();
        response.setGuestId(reservation.getGuestId());
        response.setRoomId(reservation.getRoomId());
        response.setCheckIn(reservation.getCheckIn());
        response.setCheckOut(reservation.getCheckOut());
        return response;
    }

    public void deleteReservation(String reservationId) {
        System.out.println("Service - Cancelando reserva con ID: " + reservationId);

        if (!reservasExistentes.contains(reservationId)) {
            throw new jakarta.ws.rs.NotFoundException(
                    "Reserva no encontrada con ID: " + reservationId);
        }

        System.out.println("Service - Reserva cancelada con ID: " + reservationId);
    }
}
