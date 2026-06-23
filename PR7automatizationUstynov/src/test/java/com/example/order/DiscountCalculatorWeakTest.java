package com.example.order;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * цей тест навмисно "слабкий" - він демонструє ситуацію,
 * коли тест проходить (зелений), але НЕ виявляє мутацію PIT.
 *
 * Проблема: ми перевіряємо лише точку всередині діапазону (totalPrice = 1500),
 * а не межове значення (boundary), тому мутатор, який змінює
 * "totalPrice >= 1000" на "totalPrice > 1000" (ConditionalsBoundaryMutator),
 * НЕ буде "вбитий" цим тестом - результат на 1500 не зміниться при такій мутації.
 *
 * Запуск PIT (mvn org.pitest:pitest-maven:mutationCoverage) покаже,
 * що мутація на рядку "if (totalPrice >= 1000)" виживає (SURVIVED).
 */
class DiscountCalculatorWeakTest {

    private final DiscountCalculator calculator = new DiscountCalculator();

    @Test
    void calculateDiscount_highPrice_appliesFifteenPercent() {
        double discount = calculator.calculateDiscount(1500.0, 1);

        // Слабка перевірка - не зачіпає межу 1000.0
        assertThat(discount).isEqualTo(225.0);
    }
}
