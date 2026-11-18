package com.exam.calculator;

import org.springframework.stereotype.Service;

/**
 * CalculatorService — Business / Logic Layer
 *
 * @Service tells Spring to register this class as a "Service Bean".
 * The Controller will depend on this class, and Spring will inject it
 * automatically (Dependency Injection).
 *
 * Responsibility: Contain ALL business logic.
 *   - Validate inputs (no negative numbers)
 *   - Perform the actual addition
 *   - Throw exceptions when rules are violated
 *
 * Why separate from Controller?
 *   Controllers should only handle HTTP concerns (parsing request, sending response).
 *   Business rules live in the Service so they can be tested independently.
 */
@Service
public class CalculatorService {

    /**
     * Adds two non-negative integers.
     *
     * @param a first operand (must be >= 0)
     * @param b second operand (must be >= 0)
     * @return sum of a and b
     * @throws IllegalArgumentException if either parameter is negative
     */
    public int add(int a, int b) {

        // ===== Input Validation =====
        // Requirement: reject negative parameters with a clear message
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException(
                "Parameters must be non-negative. Received: a=" + a + ", b=" + b
            );
        }

        // ===== Business Logic =====
        return a + b;
    }
}
