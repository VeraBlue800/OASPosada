package com.posada.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
public class RoomEntity {

    @Id
    @Column(name = "number")
    private Integer number;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Type {
        Sencilla,
        Doble,
        @Column(name = "Doble con aire acondicionado")
        Doble_con_aire_acondicionado
    }

    public enum Status {
        Disponible,
        Ocupada,
        No_disponible
    }

    // Getters y Setters
    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}