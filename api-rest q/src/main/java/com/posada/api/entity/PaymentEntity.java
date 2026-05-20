package com.posada.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "reservation_id", nullable = false)
    private Integer reservationId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "method", nullable = false)
    @Enumerated(EnumType.STRING)
    private Method method;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    public enum Method {
        card, cash, transfer
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getReservationId() { return reservationId; }
    public void setReservationId(Integer reservationId) { this.reservationId = reservationId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Method getMethod() { return method; }
    public void setMethod(Method method) { this.method = method; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}