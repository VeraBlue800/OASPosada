package com.posada.api.service;

import java.util.List;
import com.posada.api.entity.PaymentEntity;
import com.posada.api.entity.ReservationEntity;
import com.posada.api.mapper.PaymentMapper;
import com.posada.api.model.Payment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PaymentService {

    private static final Logger LOG = Logger.getLogger(PaymentService.class);

    @Inject
    EntityManager em;

    public boolean existsByReservationId(String reservationId) {
        Long count = em.createQuery(
                "SELECT COUNT(p) FROM PaymentEntity p WHERE p.reservationId = :reservationId", Long.class)
                .setParameter("reservationId", Integer.parseInt(reservationId))
                .getSingleResult();
        return count > 0;
    }

    @Transactional
    public Payment createPayment(Payment payment) {
        LOG.infof("Service - Registrando pago para reserva: %s", payment.getReservationId());

        // Validar que la reservación existe
        ReservationEntity reservation = em.find(ReservationEntity.class, Integer.parseInt(payment.getReservationId()));
        if (reservation == null) {
            LOG.warnf("Service - Reservación no encontrada con ID: %s", payment.getReservationId());
            throw new jakarta.ws.rs.NotFoundException(
                    "Reservación no encontrada con ID: " + payment.getReservationId());
        }

        PaymentEntity entity = PaymentMapper.toEntity(payment);
        em.persist(entity);

        LOG.infof("Service - Pago registrado con ID: %d, monto: %s via %s",
                entity.getId(), entity.getAmount(), entity.getMethod());
        return PaymentMapper.toModel(entity);
    }

    public List<Payment> getAllPayments() {
        LOG.info("Service - Obteniendo todos los pagos");
        List<PaymentEntity> entities = em.createQuery("FROM PaymentEntity", PaymentEntity.class)
                .getResultList();
        LOG.infof("Service - Total pagos encontrados: %d", entities.size());
        return entities.stream().map(PaymentMapper::toModel).toList();
    }
}