package com.posada.api.service;

import java.util.List;
import com.posada.api.entity.GuestEntity;
import com.posada.api.entity.ReservationEntity;
import com.posada.api.entity.RoomEntity;
import com.posada.api.mapper.ReservationMapper;
import com.posada.api.model.Reservation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ReservationService {

    private static final Logger LOG = Logger.getLogger(ReservationService.class);

    @Inject
    EntityManager em;

    @Transactional
    public Reservation createReservation(Reservation reservation) {
        LOG.infof("Service - Intentando crear reserva para huésped: %s", reservation.getGuestId());

        // Validar formato de IDs
        if (!reservation.getGuestId().matches("\\d+")) {
            LOG.warnf("Service - guestId inválido: %s", reservation.getGuestId());
            throw new jakarta.ws.rs.BadRequestException("El guestId debe ser un número entero válido");
        }
        if (!reservation.getRoomId().matches("\\d+")) {
            LOG.warnf("Service - roomId inválido: %s", reservation.getRoomId());
            throw new jakarta.ws.rs.BadRequestException("El roomId debe ser un número entero válido");
        }

        // Validar que el huésped existe
        GuestEntity guest = em.find(GuestEntity.class, Integer.parseInt(reservation.getGuestId()));
        if (guest == null) {
            LOG.warnf("Service - Huésped no encontrado con ID: %s", reservation.getGuestId());
            throw new jakarta.ws.rs.NotFoundException(
                    "Huésped no encontrado con ID: " + reservation.getGuestId());
        }

        // Validar que la habitación existe
        RoomEntity room = em.find(RoomEntity.class, Integer.parseInt(reservation.getRoomId()));
        if (room == null) {
            LOG.warnf("Service - Habitación no encontrada con ID: %s", reservation.getRoomId());
            throw new jakarta.ws.rs.NotFoundException(
                    "Habitación no encontrada con ID: " + reservation.getRoomId());
        }

        // Validar duplicado
        List<ReservationEntity> existing = em.createQuery(
                "FROM ReservationEntity r WHERE r.guestId = :guestId AND r.roomNumber = :roomNumber AND r.checkIn = :checkIn",
                ReservationEntity.class)
                .setParameter("guestId", Integer.parseInt(reservation.getGuestId()))
                .setParameter("roomNumber", Integer.parseInt(reservation.getRoomId()))
                .setParameter("checkIn", reservation.getCheckIn())
                .getResultList();

        if (!existing.isEmpty()) {
            LOG.warnf("Service - Conflicto: ya existe una reserva para huésped %s en habitación %s",
                    reservation.getGuestId(), reservation.getRoomId());
            throw new jakarta.ws.rs.ClientErrorException(
                    "Ya existe una reserva para este huésped en esa habitación en esa fecha",
                    Response.Status.CONFLICT);
        }

        ReservationEntity entity = ReservationMapper.toEntity(reservation);
        em.persist(entity);

        LOG.infof("Service - Reserva creada para habitación: %s", reservation.getRoomId());
        return ReservationMapper.toModel(entity);
    }

    public List<Reservation> getAllReservations() {
        LOG.info("Service - Obteniendo todas las reservas");
        List<ReservationEntity> entities = em.createQuery("FROM ReservationEntity", ReservationEntity.class)
                .getResultList();
        LOG.infof("Service - Total reservas encontradas: %d", entities.size());
        return entities.stream().map(ReservationMapper::toModel).toList();
    }

    public Reservation getReservationById(String reservationId) {
        LOG.infof("Service - Buscando reserva con ID: %s", reservationId);

        if (reservationId == null || !reservationId.matches("\\d+")) {
            LOG.warnf("Service - ID inválido recibido: %s", reservationId);
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido");
        }

        ReservationEntity entity = em.find(ReservationEntity.class, Integer.parseInt(reservationId));
        if (entity == null) {
            LOG.warnf("Service - Reserva no encontrada con ID: %s", reservationId);
            throw new jakarta.ws.rs.NotFoundException("Reserva no encontrada con ID: " + reservationId);
        }

        return ReservationMapper.toModel(entity);
    }

    @Transactional
    public Reservation updateReservation(String reservationId, Reservation reservation) {
        LOG.infof("Service - Intentando actualizar reserva con ID: %s", reservationId);

        if (reservationId == null || !reservationId.matches("\\d+")) {
            LOG.warnf("Service - ID inválido recibido: %s", reservationId);
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido: " + reservationId);
        }

        ReservationEntity entity = em.find(ReservationEntity.class, Integer.parseInt(reservationId));
        if (entity == null) {
            LOG.warnf("Service - Reserva no encontrada con ID: %s", reservationId);
            throw new jakarta.ws.rs.NotFoundException("Reserva no encontrada con ID: " + reservationId);
        }

        entity.setGuestId(Integer.parseInt(reservation.getGuestId()));
        entity.setRoomNumber(Integer.parseInt(reservation.getRoomId()));
        entity.setCheckIn(reservation.getCheckIn());
        entity.setCheckOut(reservation.getCheckOut());

        LOG.infof("Service - Reserva %s actualizada correctamente", reservationId);
        return ReservationMapper.toModel(entity);
    }

    @Transactional
    public void deleteReservation(String reservationId) {
        LOG.infof("Service - Intentando cancelar reserva con ID: %s", reservationId);

        if (reservationId == null || !reservationId.matches("\\d+")) {
            LOG.warnf("Service - ID inválido recibido: %s", reservationId);
            throw new jakarta.ws.rs.BadRequestException("El ID debe ser un número entero válido: " + reservationId);
        }

        ReservationEntity entity = em.find(ReservationEntity.class, Integer.parseInt(reservationId));
        if (entity == null) {
            LOG.warnf("Service - Reserva no encontrada con ID: %s", reservationId);
            throw new jakarta.ws.rs.NotFoundException("Reserva no encontrada con ID: " + reservationId);
        }

        em.remove(entity);
        LOG.infof("Service - Reserva %s cancelada correctamente", reservationId);
    }
}