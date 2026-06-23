package com.example.order;

public class DiscountCalculator {

    /**
     * Розраховує знижку на замовлення залежно від суми та кількості товару.
     * Має кілька гілок - гарне ціль для мутаційного тестування.
     */
    public double calculateDiscount(double totalPrice, int quantity) {
        double discount;

        if (totalPrice >= 1000) {
            discount = totalPrice * 0.15;
        } else if (totalPrice >= 500) {
            discount = totalPrice * 0.10;
        } else if (quantity >= 10) {
            discount = totalPrice * 0.05;
        } else {
            discount = 0.0;
        }

        return discount;
    }
}
