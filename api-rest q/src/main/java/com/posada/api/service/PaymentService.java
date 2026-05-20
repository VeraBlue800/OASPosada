package com.posada.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.posada.api.model.Payment;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PaymentService {

    private static final Logger LOG = Logger.getLogger(PaymentService.class);

    private final Map<String, Payment> database = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(3);

    @PostConstruct
    void init() {
        Payment p1 = new Payment();
        p1.setReservationId("1");
        p1.setAmount(new BigDecimal("1200.00"));
        p1.setMethod(Payment.MethodEnum.fromValue("cash"));
        p1.setDate(LocalDate.of(2025, 6, 1));
        database.put("1", p1);

        Payment p2 = new Payment();
        p2.setReservationId("2");
        p2.setAmount(new BigDecimal("2400.00"));
        p2.setMethod(Payment.MethodEnum.fromValue("card"));
        p2.setDate(LocalDate.of(2025, 6, 10));
        database.put("2", p2);

        LOG.info("Service - Datos precargados: 2 pagos registrados");
    }

    // Verifica si ya existe un pago para esa reserva
    public boolean existsByReservationId(String reservationId) {
        return database.values().stream()
                .anyMatch(p -> reservationId.equals(p.getReservationId()));
    }

    public Payment createPayment(Payment payment) {
        LOG.infof("Service - Registrando pago para reserva: %s", payment.getReservationId());

        String newId = String.valueOf(idCounter.getAndIncrement());

        Payment response = new Payment();
        response.setReservationId(payment.getReservationId());
        response.setAmount(payment.getAmount());
        response.setMethod(payment.getMethod());
        response.setDate(payment.getDate());

        database.put(newId, response);

        LOG.infof("Service - Pago registrado con ID: %s, monto: %s via %s", newId, response.getAmount(), response.getMethod());
        return response;
    }

    public List<Payment> getAllPayments() {
        LOG.infof("Service - Obteniendo todos los pagos, total: %d", database.size());
        return new ArrayList<>(database.values());
    }
}