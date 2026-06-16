package com.university;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

public class NumberProcessorTest {

    private NumberProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new NumberProcessor();
    }

    // 1. ПРОСТИЙ ТЕСТ
    @Test
    @Tag("basic") // Тег для базових перевірок
    @DisplayName("Простий тест: Перевірка додавання")
    void testSimpleAddition() {
        int result = processor.add(5, 5);
        assertEquals(10, result, "5 + 5 має дорівнювати 10");
    }

    // 2. ПАРАМЕТРИЗОВАНИЙ ТЕСТ З 1 ПАРАМЕТРОМ (Статичні параметри)
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8, -6})
    @Tag("basic")
    @DisplayName("Параметровий (1 параметр): Перевірка парних чисел")
    void testIsEven(int number) {
        // Припущення: логічний сенс тестувати парність тільки для додатних чисел у цьому контексті
        assumingThat(number > 0, () -> {
            assertTrue(processor.isEven(number), "Число " + number + " має бути парним");
        });
    }

    // 3. ПАРАМЕТРИЗОВАНИЙ ТЕСТ З НАБОРОМ ПАРАМЕТРІВ (Статичні параметри)
    @ParameterizedTest
    @CsvSource({
            "10, 2, 5",
            "100, 10, 10",
            "15, 3, 5"
    })
    @Tag("advanced") // Тег для складніших розрахунків
    @DisplayName("Параметровий (набір): Перевірка ділення")
    void testDivision(int a, int b, int expected) {
        // Припущення: продовжувати тест, тільки якщо дільник не нуль
        assumeTrue(b != 0, "Дільник не може бути нулем для цього тесту");

        assertEquals(expected, processor.divide(a, b));
    }

    // 4. ДИНАМІЧНИЙ ТЕСТ (@TestFactory)
    @TestFactory
    @Tag("dynamic") // Окремий тег для динамічних тестів
    @DisplayName("Динамічний тест: Генерація тестів додавання нуля")
    Collection<DynamicTest> dynamicTests() {
        // Припущення на рівні фабрики тестів
        String currentOS = System.getProperty("os.name");
        assumeTrue(currentOS.contains("Windows") || currentOS.contains("Linux") || currentOS.contains("Mac"),
                "Запускати ці динамічні тести тільки на відомих ОС");

        return Arrays.asList(
                DynamicTest.dynamicTest("Додавання 0 до 5", () -> assertEquals(5, processor.add(5, 0))),
                DynamicTest.dynamicTest("Додавання 0 до 100", () -> assertEquals(100, processor.add(100, 0))),
                DynamicTest.dynamicTest("Додавання 0 до -1", () -> assertEquals(-1, processor.add(-1, 0)))
        );
    }
}