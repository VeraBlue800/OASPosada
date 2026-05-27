package com.posada.api.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
public class RoomEntity {

    @Id
    @Column(name = "number")
    private Integer number;

    @Column(name = "type", nullable = false)
    @Convert(converter = TypeConverter.class)
    private Type type;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "status")
    @Convert(converter = StatusConverter.class)
    private Status status;

    public enum Type {
        Sencilla("Sencilla"),
        Doble("Doble"),
        Doble_con_aire_acondicionado("Doble con aire acondicionado");

        private final String dbValue;

        Type(String dbValue) { this.dbValue = dbValue; }

        @JsonValue
        public String getDbValue() { return dbValue; }

        @JsonCreator
        public static Type fromValue(String value) {
            for (Type t : values()) {
                if (t.dbValue.equals(value)) return t;
            }
            throw new IllegalArgumentException("Unknown type: " + value);
        }
    }

    public enum Status {
        Disponible("Disponible"),
        Ocupada("Ocupada"),
        No_disponible("No disponible");

        private final String dbValue;

        Status(String dbValue) { this.dbValue = dbValue; }

        @JsonValue
        public String getDbValue() { return dbValue; }

        @JsonCreator
        public static Status fromValue(String value) {
            for (Status s : values()) {
                if (s.dbValue.equals(value)) return s;
            }
            throw new IllegalArgumentException("Unknown status: " + value);
        }
    }

    @Converter
    public static class TypeConverter implements AttributeConverter<Type, String> {
        @Override
        public String convertToDatabaseColumn(Type type) {
            return type == null ? null : type.getDbValue();
        }

        @Override
        public Type convertToEntityAttribute(String dbData) {
            return dbData == null ? null : Type.fromValue(dbData);
        }
    }

    @Converter
    public static class StatusConverter implements AttributeConverter<Status, String> {
        @Override
        public String convertToDatabaseColumn(Status status) {
            return status == null ? null : status.getDbValue();
        }

        @Override
        public Status convertToEntityAttribute(String dbData) {
            return dbData == null ? null : Status.fromValue(dbData);
        }
    }

    // Getters y Setters (sin cambios)
    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}