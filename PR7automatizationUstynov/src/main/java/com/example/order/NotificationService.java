package com.example.order;

public interface NotificationService {

    void notifyOrderCompleted(String orderId);

    void notifyOutOfStock(String orderId);

    void notifyPaymentFailed(String orderId);
}
