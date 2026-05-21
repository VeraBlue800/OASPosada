package com.posada.api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.posada.api.model.Reservation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ReservationService {

    private static final Logger LOG = Logger.getLogger(ReservationService.class);

    private final List<Reservation> reservations = new ArrayList<>(List.of(
            crearReservation("1", "101", LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 5)),
            crearReservation("2", "102", LocalDate.of(2025, 6, 10), LocalDate.of(2025, 6, 15))
    ));

    private Reservation crearReservation(String guestId, String roomId, LocalDate checkIn, LocalDate checkOut) {
        Reservation r = new Reservation();
        r.setGuestId(guestId);
        r.setRoomId(roomId);
        r.setCheckIn(checkIn);
        r.setCheckOut(checkOut);
        return r;
    }

    public Reservation createReservation(Reservation reservation) {
        LOG.infof("Service - Intentando crear reserva para huésped: %s", reservation.getGuestId());

        boolean exists = reservations.stream()
                .anyMatch(r -> r.getGuestId().equals(reservation.getGuestId())
                        && r.getRoomId().equals(reservation.getRoomId())
                        && r.getCheckIn().equals(reservation.getCheckIn()));
        if (exists) {
            LOG.warnf("Service - Conflicto: ya existe una reserva para huésped %s en habitación %s",
                    reservation.getGuestId(), reservation.getRoomId());
            throw new jakarta.ws.rs.ClientErrorException(
                    "Ya existe una reserva para este huésped en esa habitación en esa fecha",
                    Response.Status.CONFLICT);
        }

        Reservation response = crearReservation(
                reservation.getGuestId(),
                reservation.getRoomId(),
                reservation.getCheckIn(),
                reservation.getCheckOut());

        reservations.add(response);

        LOG.infof("Service - Reserva creada para habitación: %s", response.getRoomId());
        return response;
    }

    public List<Reservation> getAllReservations() {
        LOG.infof("Service - Obteniendo todas las reservas, total: %d", reservations.size());
        return new ArrayList<>(reservations);
    }

    public Reservation getReservationById(String reservationId) {
        LOG.infof("Service - Buscando reserva con ID: %s", reservationId);

        if (reservationId == null || !reservationId.matches("\\d+")) {
            LOG.warnf("Service - ID inválido recibido: %s", reservationId);
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido");
        }

        return reservations.stream()
                .filter(r -> r.getGuestId().equals(reservationId))
                .findFirst()
                .orElseThrow(() -> {
                    LOG.warnf("Service - Reserva no encontrada con ID: %s", reservationId);
                    return new jakarta.ws.rs.NotFoundException("Reserva no encontrada con ID: " + reservationId);
                });
    }

    public Reservation updateReservation(String reservationId, Reservation reservation) {
        LOG.infof("Service - Intentando actualizar reserva con ID: %s", reservationId);

        if (reservationId == null || !reservationId.matches("\\d+")) {
            LOG.warnf("Service - ID inválido recibido: %s", reservationId);
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido: " + reservationId);
        }

        Reservation existing = reservations.stream()
                .filter(r -> r.getGuestId().equals(reservationId))
                .findFirst()
                .orElseThrow(() -> {
                    LOG.warnf("Service - Reserva no encontrada con ID: %s", reservationId);
                    return new jakarta.ws.rs.NotFoundException("Reserva no encontrada con ID: " + reservationId);
                });

        existing.setGuestId(reservation.getGuestId());
        existing.setRoomId(reservation.getRoomId());
        existing.setCheckIn(reservation.getCheckIn());
        existing.setCheckOut(reservation.getCheckOut());

        LOG.infof("Service - Reserva %s actualizada correctamente", reservationId);
        return existing;
    }

    public void deleteReservation(String reservationId) {
        LOG.infof("Service - Intentando cancelar reserva con ID: %s", reservationId);

        if (reservationId == null || !reservationId.matches("\\d+")) {
            LOG.warnf("Service - ID inválido recibido: %s", reservationId);
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido: " + reservationId);
        }

        Reservation existing = reservations.stream()
                .filter(r -> r.getGuestId().equals(reservationId))
                .findFirst()
                .orElseThrow(() -> {
                    LOG.warnf("Service - Reserva no encontrada con ID: %s", reservationId);
                    return new jakarta.ws.rs.NotFoundException("Reserva no encontrada con ID: " + reservationId);
                });

        reservations.remove(existing);
        LOG.infof("Service - Reserva %s cancelada correctamente", reservationId);
    }
}