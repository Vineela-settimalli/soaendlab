package com.exam.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CalculatorServiceTest — Pure Unit Tests (no Spring context)
 *
 * This class tests the SERVICE layer in complete isolation.
 * No Spring, no HTTP, no Mockito — just plain Java objects.
 *
 * Key Annotations:
 * ─────────────────────────────────────────────────────────────
 * @Test        : Marks a method as a JUnit 5 test case.
 *                JUnit finds and runs every @Test method automatically.
 *
 * @BeforeEach  : This method runs BEFORE each @Test method.
 *                Used to set up fresh test state (avoids shared mutable state).
 *
 * @DisplayName : Sets a human-readable label for the test shown in IDE/reports.
 *                Makes test output easier to read.
 *
 * Key Assertions (from JUnit 5):
 * ─────────────────────────────────────────────────────────────
 * assertEquals(expected, actual)
 *   → Passes if expected value equals actual value.
 *   → Example: assertEquals(30, service.add(10, 20))
 *
 * assertThrows(ExceptionType.class, () -> { ... })
 *   → Passes if the lambda THROWS the specified exception.
 *   → Returns the exception so you can inspect its message.
 *   → Example: assertThrows(IllegalArgumentException.class, () -> service.add(-1, 5))
 */
class CalculatorServiceTest {

    // The real service — no mocks here, we test the actual implementation
    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        // Create a fresh instance before every test
        calculatorService = new CalculatorService();
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 1: Normal addition — 10 + 20 = 30
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Normal addition: 10 + 20 should return 30")
    void testAdd_NormalValues_ReturnsSum() {
        // Arrange: inputs
        int a = 10, b = 20;

        // Act: call the method under test
        int result = calculatorService.add(a, b);

        // Assert: verify the result
        assertEquals(30, result, "10 + 20 should equal 30");
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 2: Addition with zero
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Addition with zero: 5 + 0 should return 5")
    void testAdd_WithZero_ReturnsCorrectSum() {
        int result = calculatorService.add(5, 0);
        assertEquals(5, result, "5 + 0 should equal 5");
    }

    @Test
    @DisplayName("Both zeros: 0 + 0 should return 0")
    void testAdd_BothZeros_ReturnsZero() {
        int result = calculatorService.add(0, 0);
        assertEquals(0, result, "0 + 0 should equal 0");
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 3: Negative first parameter — should throw exception
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Negative first parameter should throw IllegalArgumentException")
    void testAdd_NegativeFirstParam_ThrowsException() {

        // assertThrows: verify an exception IS thrown
        // The lambda () -> ... is the code that should throw
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calculatorService.add(-1, 5),
            "Should throw IllegalArgumentException for negative 'a'"
        );

        // Also verify the exception message contains useful info
        assertTrue(exception.getMessage().contains("non-negative"),
            "Exception message should mention 'non-negative'");
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 4: Negative second parameter — should throw exception
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Negative second parameter should throw IllegalArgumentException")
    void testAdd_NegativeSecondParam_ThrowsException() {

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calculatorService.add(5, -3),
            "Should throw IllegalArgumentException for negative 'b'"
        );

        assertTrue(exception.getMessage().contains("non-negative"),
            "Exception message should mention 'non-negative'");
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 5: Both parameters negative
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Both parameters negative should throw IllegalArgumentException")
    void testAdd_BothNegative_ThrowsException() {

        assertThrows(
            IllegalArgumentException.class,
            () -> calculatorService.add(-1, -1),
            "Should throw IllegalArgumentException when both are negative"
        );
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 6: Large numbers
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Large numbers: 1000 + 2000 should return 3000")
    void testAdd_LargeNumbers_ReturnsCorrectSum() {
        assertEquals(3000, calculatorService.add(1000, 2000));
    }
}
