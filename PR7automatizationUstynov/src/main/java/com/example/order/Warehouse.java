package com.example.order;

public interface Warehouse {

    /**
     * Перевіряє, чи достатньо товару на складі.
     */
    boolean isAvailable(String productCode, int quantity);

    /**
     * Списує товар зі складу.
     */
    void reserve(String productCode, int quantity);
}
