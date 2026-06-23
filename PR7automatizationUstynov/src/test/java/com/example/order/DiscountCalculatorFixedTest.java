package com.example.order;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class DiscountCalculatorFixedTest {

    private final DiscountCalculator calculator = new DiscountCalculator();

    @Test
    void calculateDiscount_exactlyAtThousandBoundary_appliesFifteenPercent() {

        double discount = calculator.calculateDiscount(1000.0, 1);

        assertThat(discount).isEqualTo(150.0); // 1000 * 0.15
    }

    @Test
    void calculateDiscount_justBelowThousandBoundary_appliesTenPercent() {

        double discount = calculator.calculateDiscount(999.99, 1);


        assertThat(discount).isCloseTo(99.999, within(0.0001)); // 999.99 * 0.10
    }

    @Test
    void calculateDiscount_exactlyAtFiveHundredBoundary_appliesTenPercent() {
        double discount = calculator.calculateDiscount(500.0, 1);

        assertThat(discount).isEqualTo(50.0); // 500 * 0.10
    }

    @Test
    void calculateDiscount_justBelowFiveHundredBoundary_withHighQuantity_appliesFivePercent() {
        double discount = calculator.calculateDiscount(499.99, 10);

        assertThat(discount).isCloseTo(24.9995, within(0.0001)); // 499.99 * 0.05
    }

    @Test
    void calculateDiscount_belowAllThresholds_appliesNoDiscount() {
        double discount = calculator.calculateDiscount(100.0, 1);

        assertThat(discount).isEqualTo(0.0);
    }
}