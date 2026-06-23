package com.example.order;

import java.util.Objects;

public class Order {

    private final String id;
    private final String productCode;
    private final int quantity;
    private final double totalPrice;
    private OrderStatus status;

    public Order(String id, String productCode, int quantity, double totalPrice) {
        this.id = id;
        this.productCode = productCode;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = OrderStatus.NEW;
    }

    public String getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{id='" + id + "', productCode='" + productCode +
                "', quantity=" + quantity + ", totalPrice=" + totalPrice +
                ", status=" + status + "}";
    }
}
