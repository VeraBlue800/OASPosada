package com.posada.api.service;
 
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
 
import com.posada.api.model.Payment;
 
import jakarta.enterprise.context.ApplicationScoped; // <-- Importante
 
@ApplicationScoped // Le dice a Quarkus que gestione esta clase como un servicio
public class PaymentService {
 
    public Payment createPayment(Payment payment) {
        System.out.println("Service - Pago recibido para reserva: " + payment.getReservationId());
 
        // Validación del método de pago (el OAS define: card, cash, transfer)
        String method = payment.getMethod().getValue();
        if (!method.equals("card") && !method.equals("cash") && !method.equals("transfer")) {
            throw new jakarta.ws.rs.BadRequestException(
                "Método de pago inválido: " + method + ". Valores permitidos: card, cash, transfer");
        }
 
        // Simulamos la persistencia y generación de datos del sistema
        Payment response = new Payment();
 
        // Mapeo de campos desde el Request
        // NOTA: El modelo Payment no tiene campo 'id' (no está en el OAS schema)
        response.setReservationId(payment.getReservationId());
        response.setAmount(payment.getAmount());
        response.setMethod(payment.getMethod());
        response.setDate(payment.getDate());
 
        System.out.println("Service - Pago registrado: " + response.getAmount() + " via " + response.getMethod());
        return response;
    }
 
    public List<Payment> getAllPayments() {
        System.out.println("Service - Obteniendo todos los pagos");
 
        // Simulación de búsqueda en base de datos
        List<Payment> payments = new ArrayList<>();
 
        Payment p1 = new Payment();
        p1.setReservationId("1");
        p1.setAmount(new BigDecimal("1200.00"));
        p1.setMethod(Payment.MethodEnum.fromValue("cash"));
        p1.setDate(LocalDate.of(2025, 6, 1));
        payments.add(p1);
 
        Payment p2 = new Payment();
        p2.setReservationId("2");
        p2.setAmount(new BigDecimal("2400.00"));
        p2.setMethod(Payment.MethodEnum.fromValue("card"));
        p2.setDate(LocalDate.of(2025, 6, 10));
        payments.add(p2);
 
        return payments;
    }
}
 
