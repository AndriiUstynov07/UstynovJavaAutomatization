package com.example.order;

import java.util.List;
import java.util.Objects;

public class OrderProcessor {

    private final Warehouse warehouse;
    private final PaymentGateway paymentGateway;
    private final NotificationService notificationService;

    public OrderProcessor(Warehouse warehouse,
                           PaymentGateway paymentGateway,
                           NotificationService notificationService) {
        this.warehouse = warehouse;
        this.paymentGateway = paymentGateway;
        this.notificationService = notificationService;
    }

    /**
     * Обробляє одне замовлення:
     * 1) перевіряє наявність товару на складі
     * 2) якщо товару немає - статус OUT_OF_STOCK, сповіщення, вихід
     * 3) якщо товар є - намагається провести оплату
     * 4) якщо оплата не пройшла - статус PAYMENT_FAILED, сповіщення, вихід
     * 5) якщо оплата пройшла - резервує товар, статус COMPLETED, сповіщення
     */
    public OrderStatus processOrder(Order order) {
        Objects.requireNonNull(order, "order must not be null");

        boolean available = warehouse.isAvailable(order.getProductCode(), order.getQuantity());

        if (!available) {
            order.setStatus(OrderStatus.OUT_OF_STOCK);
            notificationService.notifyOutOfStock(order.getId());
            return order.getStatus();
        }

        boolean paid = paymentGateway.charge(order.getId(), order.getTotalPrice());

        if (!paid) {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
            notificationService.notifyPaymentFailed(order.getId());
            return order.getStatus();
        }

        warehouse.reserve(order.getProductCode(), order.getQuantity());
        order.setStatus(OrderStatus.COMPLETED);
        notificationService.notifyOrderCompleted(order.getId());

        return order.getStatus();
    }

    /**
     * Обробляє список замовлень і повертає лише ті, що завершились успішно.
     * Метод із додатковою гілкою (порожній список / фільтрація) - корисний
     * для AssertJ-перевірок списків.
     */
    public List<Order> processBatch(List<Order> orders) {
        Objects.requireNonNull(orders, "orders must not be null");

        if (orders.isEmpty()) {
            return List.of();
        }

        return orders.stream()
                .peek(this::processOrder)
                .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                .toList();
    }
}
