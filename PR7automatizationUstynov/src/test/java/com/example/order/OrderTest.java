package com.example.order;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Прицільні тести для Order.equals()/hashCode()/toString(),
 * щоб покрити гілки, які не зачіпаються через OrderProcessorTest
 * (там Order використовується лише опосередковано).
 */
class OrderTest {

    @Test
    void equals_sameReference_returnsTrue() {
        Order order = new Order("ORD-1", "SKU-100", 1, 50.0);

        // this == o - рефлексивність
        assertThat(order.equals(order)).isTrue();
    }

    @Test
    void equals_differentType_returnsFalse() {
        Order order = new Order("ORD-1", "SKU-100", 1, 50.0);

        // !(o instanceof Order)
        assertThat(order.equals("not an order")).isFalse();
    }

    @Test
    void equals_null_returnsFalse() {
        Order order = new Order("ORD-1", "SKU-100", 1, 50.0);

        assertThat(order.equals(null)).isFalse();
    }

    @Test
    void equals_sameId_returnsTrue() {
        Order order1 = new Order("ORD-1", "SKU-100", 1, 50.0);
        Order order2 = new Order("ORD-1", "SKU-200", 99, 999.0);

        // equals базується лише на id
        assertThat(order1).isEqualTo(order2);
    }

    @Test
    void equals_differentId_returnsFalse() {
        Order order1 = new Order("ORD-1", "SKU-100", 1, 50.0);
        Order order2 = new Order("ORD-2", "SKU-100", 1, 50.0);

        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void hashCode_sameId_returnsSameHashCode() {
        Order order1 = new Order("ORD-1", "SKU-100", 1, 50.0);
        Order order2 = new Order("ORD-1", "SKU-999", 5, 5.0);

        assertThat(order1.hashCode()).isEqualTo(order2.hashCode());
    }

    @Test
    void toString_containsAllFields() {
        Order order = new Order("ORD-1", "SKU-100", 3, 150.0);

        String result = order.toString();

        assertThat(result)
                .contains("ORD-1")
                .contains("SKU-100")
                .contains("3")
                .contains("150.0")
                .contains("NEW");
    }

    @Test
    void setStatus_updatesStatus() {
        Order order = new Order("ORD-1", "SKU-100", 1, 50.0);

        order.setStatus(OrderStatus.COMPLETED);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    void newOrder_hasInitialStatusNew() {
        Order order = new Order("ORD-1", "SKU-100", 1, 50.0);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.NEW);
    }
}