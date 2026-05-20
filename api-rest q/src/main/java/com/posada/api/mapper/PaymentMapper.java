package com.posada.api.mapper;

import com.posada.api.entity.PaymentEntity;
import com.posada.api.model.Payment;

public class PaymentMapper {

    // Convierte de modelo (API) a Entity (BD)
    public static PaymentEntity toEntity(Payment payment) {
        PaymentEntity entity = new PaymentEntity();
        entity.setReservationId(Integer.parseInt(payment.getReservationId()));
        entity.setAmount(payment.getAmount());
        entity.setMethod(PaymentEntity.Method.valueOf(payment.getMethod().getValue()));
        entity.setDate(payment.getDate());
        return entity;
    }

    // Convierte de Entity (BD) a modelo (API)
    public static Payment toModel(PaymentEntity entity) {
        Payment payment = new Payment();
        payment.setReservationId(String.valueOf(entity.getReservationId()));
        payment.setAmount(entity.getAmount());
        payment.setMethod(Payment.MethodEnum.fromValue(entity.getMethod().name()));
        payment.setDate(entity.getDate());
        return payment;
    }
}