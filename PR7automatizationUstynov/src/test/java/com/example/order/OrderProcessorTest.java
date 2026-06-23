package com.example.order;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderProcessorTest {

    @Mock
    private Warehouse warehouse;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private OrderProcessor orderProcessor;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order("ORD-1", "SKU-100", 2, 199.99);
    }


    // 1) Mockito: щонайменше 3 різні бізнес-сценарії

    @Test
    @DisplayName("Сценарій 1: товару немає на складі -> OUT_OF_STOCK, оплата не викликається")
    void processOrder_outOfStock_returnsOutOfStockStatus() {
        when(warehouse.isAvailable("SKU-100", 2)).thenReturn(false);

        OrderStatus result = orderProcessor.processOrder(order);

        assertThat(result).isEqualTo(OrderStatus.OUT_OF_STOCK);
        verify(notificationService).notifyOutOfStock("ORD-1");
    }

    @Test
    @DisplayName("Сценарій 2: товар є, але оплата не пройшла -> PAYMENT_FAILED")
    void processOrder_paymentFails_returnsPaymentFailedStatus() {
        when(warehouse.isAvailable("SKU-100", 2)).thenReturn(true);
        when(paymentGateway.charge("ORD-1", 199.99)).thenReturn(false);

        OrderStatus result = orderProcessor.processOrder(order);

        assertThat(result).isEqualTo(OrderStatus.PAYMENT_FAILED);
        verify(notificationService).notifyPaymentFailed("ORD-1");
    }

    @Test
    @DisplayName("Сценарій 3: товар є і оплата успішна -> COMPLETED, товар резервується")
    void processOrder_happyPath_returnsCompletedStatus() {
        when(warehouse.isAvailable("SKU-100", 2)).thenReturn(true);
        when(paymentGateway.charge("ORD-1", 199.99)).thenReturn(true);

        OrderStatus result = orderProcessor.processOrder(order);

        assertThat(result).isEqualTo(OrderStatus.COMPLETED);
    }


    // 2) verify / times / never для void-методів


    @Test
    @DisplayName("verify: при успішному замовленні викликається саме notifyOrderCompleted")
    void processOrder_happyPath_verifiesNotificationCalled() {
        when(warehouse.isAvailable("SKU-100", 2)).thenReturn(true);
        when(paymentGateway.charge("ORD-1", 199.99)).thenReturn(true);

        orderProcessor.processOrder(order);

        verify(notificationService).notifyOrderCompleted("ORD-1");
        verify(warehouse).reserve("SKU-100", 2);
    }

    @Test
    @DisplayName("times: повторна обробка одного й того ж замовлення двічі -> reserve викликається рівно 2 рази")
    void processOrder_calledTwice_reserveInvokedTwoTimes() {
        when(warehouse.isAvailable("SKU-100", 2)).thenReturn(true);
        when(paymentGateway.charge("ORD-1", 199.99)).thenReturn(true);

        orderProcessor.processOrder(order);
        orderProcessor.processOrder(order);

        verify(warehouse, times(2)).reserve("SKU-100", 2);
        verify(notificationService, times(2)).notifyOrderCompleted("ORD-1");
    }

    @Test
    @DisplayName("never: якщо товару немає на складі, оплата і резервування НЕ викликаються")
    void processOrder_outOfStock_paymentAndReserveNeverCalled() {
        when(warehouse.isAvailable("SKU-100", 2)).thenReturn(false);

        orderProcessor.processOrder(order);

        verify(paymentGateway, never()).charge("ORD-1", 199.99);
        verify(warehouse, never()).reserve("SKU-100", 2);
        verify(notificationService, never()).notifyOrderCompleted("ORD-1");
    }


    // 3) AssertJ SoftAssertions

    @Test
    @DisplayName("SoftAssertions: перевірка кількох незалежних умов після успішної обробки")
    void processOrder_happyPath_softAssertionsOnOrderState() {
        when(warehouse.isAvailable("SKU-100", 2)).thenReturn(true);
        when(paymentGateway.charge("ORD-1", 199.99)).thenReturn(true);

        OrderStatus result = orderProcessor.processOrder(order);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result).isEqualTo(OrderStatus.COMPLETED);
        softly.assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        softly.assertThat(order.getId()).isEqualTo("ORD-1");
        softly.assertThat(order.getQuantity()).isPositive();
        softly.assertThat(order.getTotalPrice()).isGreaterThan(0.0);
        softly.assertAll();
    }


    // 4) AssertJ: перевірки списків (щонайменше 3 різні типи)// ---------------------------------------------------------------

    @Test
    @DisplayName("Списки: processBatch повертає лише завершені замовлення (containsExactly, hasSize, allMatch)")
    void processBatch_mixedOrders_returnsOnlyCompletedOnes() {
        Order order1 = new Order("ORD-1", "SKU-100", 1, 50.0);
        Order order2 = new Order("ORD-2", "SKU-200", 1, 70.0);
        Order order3 = new Order("ORD-3", "SKU-300", 1, 90.0);

        when(warehouse.isAvailable("SKU-100", 1)).thenReturn(true);
        when(paymentGateway.charge("ORD-1", 50.0)).thenReturn(true);

        when(warehouse.isAvailable("SKU-200", 1)).thenReturn(false);

        when(warehouse.isAvailable("SKU-300", 1)).thenReturn(true);
        when(paymentGateway.charge("ORD-3", 90.0)).thenReturn(true);

        List<Order> completedOrders = orderProcessor.processBatch(List.of(order1, order2, order3));

        // Перевірка 1: розмір списку
        assertThat(completedOrders).hasSize(2);

        // Перевірка 2: точний вміст (без врахування порядку)
        assertThat(completedOrders).containsExactlyInAnyOrder(order1, order3);

        // Перевірка 3: усі елементи відповідають умові
        assertThat(completedOrders).allMatch(o -> o.getStatus() == OrderStatus.COMPLETED);

        // Додатково: список НЕ містить замовлення, якого не вистачило на складі
        assertThat(completedOrders).doesNotContain(order2);
    }

    @Test
    @DisplayName("Списки: порожній вхідний список -> processBatch повертає порожній список")
    void processBatch_emptyInput_returnsEmptyList() {
        List<Order> result = orderProcessor.processBatch(List.of());

        assertThat(result).isEmpty();
        assertThat(result).isInstanceOf(List.class);
    }

    @Test
    @DisplayName("Списки: extracting - перевірка конкретних полів елементів списку")
    void processBatch_extractingOrderIds() {
        Order order1 = new Order("ORD-1", "SKU-100", 1, 50.0);
        Order order2 = new Order("ORD-2", "SKU-200", 1, 70.0);

        when(warehouse.isAvailable("SKU-100", 1)).thenReturn(true);
        when(paymentGateway.charge("ORD-1", 50.0)).thenReturn(true);

        when(warehouse.isAvailable("SKU-200", 1)).thenReturn(true);
        when(paymentGateway.charge("ORD-2", 70.0)).thenReturn(true);

        List<Order> completedOrders = orderProcessor.processBatch(List.of(order1, order2));

        assertThat(completedOrders)
                .extracting(Order::getId)
                .containsExactlyInAnyOrder("ORD-1", "ORD-2");
    }
}