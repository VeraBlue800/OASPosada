package com.ejemplo.api.service;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
 
import com.ejemplo.api.model.Reservation;
 
import jakarta.enterprise.context.ApplicationScoped;
 
@ApplicationScoped
public class ReservationService {
 
    public Reservation createReservation(Reservation reservation) {
        System.out.println("Service - Reserva recibida para huésped: " + reservation.getGuestId());
 
        // Simulamos la persistencia y generación de datos del sistema
        Reservation response = new Reservation();
 
        // Mapeo de campos desde el Request
        // NOTA: El modelo Reservation no tiene campo 'id' (no está en el OAS schema)
        response.setGuestId(reservation.getGuestId());
        response.setRoomId(reservation.getRoomId());
        response.setCheckIn(reservation.getCheckIn());
        response.setCheckOut(reservation.getCheckOut());
 
        System.out.println("Service - Reserva creada para habitación: " + response.getRoomId());
        return response;
    }
 
    public List<Reservation> getAllReservations() {
        System.out.println("Service - Obteniendo todas las reservas");
 
        // Simulación de consulta a base de datos
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
 
        System.out.println("Service - Reservas encontradas: " + reservations.size());
        return reservations;
    }
 
    public Reservation getReservationById(String reservationId) {
        System.out.println("Service - Buscando reserva con ID: " + reservationId);
 
        // Simulación de búsqueda en base de datos
        // En un caso real: return repositorio.findById(reservationId)
        // Simulamos que solo existe la reserva con ID "1"
        if (!"1".equals(reservationId)) {
            return null; // El Resource se encargará de responder 404
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
 
        // Simulamos que solo existe la reserva con ID "1"
        if (!"1".equals(reservationId)) {
            return null; // El Resource se encargará de responder 404
        }
 
        Reservation response = new Reservation();
        response.setGuestId(reservation.getGuestId());
        response.setRoomId(reservation.getRoomId());
        response.setCheckIn(reservation.getCheckIn());
        response.setCheckOut(reservation.getCheckOut());
 
        System.out.println("Service - Reserva actualizada para habitación: " + response.getRoomId());
        return response;
    }
 
    public boolean deleteReservation(String reservationId) {
        System.out.println("Service - Cancelando reserva con ID: " + reservationId);
 
        // Simulamos que solo existe la reserva con ID "1"
        if (!"1".equals(reservationId)) {
            return false; // El Resource se encargará de responder 404
        }
 
        // En un caso real: repositorio.deleteById(reservationId)
        System.out.println("Service - Reserva cancelada con ID: " + reservationId);
        return true;
    }
}
 
