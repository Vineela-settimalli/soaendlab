package com.exam.calculator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

/**
 * CalculatorController — HTTP / Presentation Layer
 *
 * @RestController = @Controller + @ResponseBody
 *   - @Controller  : marks this as a Spring MVC controller (handles HTTP requests)
 *   - @ResponseBody: automatically converts return values to JSON (via Jackson)
 *
 * @RequestMapping("/calculator") maps ALL endpoints in this class under /calculator.
 *
 * Responsibility: Handle HTTP requests and responses ONLY.
 *   - Parse query parameters
 *   - Delegate logic to CalculatorService
 *   - Convert service results (or exceptions) into proper HTTP responses
 *
 * Constructor Injection:
 *   Spring sees the single constructor and automatically injects the
 *   CalculatorService bean. This is preferred over @Autowired field injection
 *   because it makes dependencies explicit and testable.
 */
@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    // ===== Dependency (injected via constructor) =====
    private final CalculatorService calculatorService;

    // Constructor Injection — Spring calls this constructor and passes the Service bean
    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    // =========================================================
    //  GET /calculator/add?a=10&b=20
    // =========================================================
    /**
     * Adds two integers provided as query parameters.
     *
     * @GetMapping("/add") — handles HTTP GET requests to /calculator/add
     * @RequestParam       — binds query string parameters to method arguments
     *
     * Flow:
     *   1. Spring extracts ?a=10&b=20 from the URL
     *   2. Calls calculatorService.add(a, b)
     *   3. If service succeeds → returns 200 OK with the result
     *   4. If service throws IllegalArgumentException → caught here → 400 Bad Request
     */
    @GetMapping("/add")
    public ResponseEntity<?> add(
            @RequestParam int a,
            @RequestParam int b) {

        try {
            int result = calculatorService.add(a, b);

            // 200 OK — wrap result in a readable message
            return ResponseEntity.ok("Result: " + a + " + " + b + " = " + result);

        } catch (IllegalArgumentException e) {

            // 400 Bad Request — return the exception message as the error body
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    // =========================================================
    //  Handle missing required query parameters
    //  e.g., GET /calculator/add?a=10   (b is missing)
    // =========================================================
    /**
     * @ExceptionHandler catches specific exceptions thrown anywhere in this controller.
     * MissingServletRequestParameterException is thrown by Spring when a required
     * @RequestParam is absent from the URL.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParam(
            MissingServletRequestParameterException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error: Missing required parameter '" + ex.getParameterName() + "'");
    }
}
