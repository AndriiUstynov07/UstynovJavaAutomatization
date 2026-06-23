package com.example.order;

public interface PaymentGateway {

    /**
     * Намагається провести оплату.
     * @return true, якщо оплата успішна
     */
    boolean charge(String orderId, double amount);
}
