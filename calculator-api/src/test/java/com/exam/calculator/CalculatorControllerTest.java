package com.exam.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CalculatorControllerTest — Controller Layer Test using MockMvc + Mockito
 *
 * This tests the CONTROLLER in isolation from the real service.
 * Mockito replaces the real CalculatorService with a "fake" (mock) object
 * so we can control exactly what the service returns for each test scenario.
 *
 * Key Annotations:
 * ─────────────────────────────────────────────────────────────
 * @WebMvcTest(CalculatorController.class)
 *   → Loads ONLY the web layer (controllers, filters, MVC config).
 *   → Does NOT load @Service, @Repository, or the full Spring context.
 *   → Much faster than @SpringBootTest.
 *   → Automatically configures MockMvc for you.
 *
 * @Autowired MockMvc
 *   → MockMvc simulates HTTP requests without actually starting a real
 *     HTTP server. It calls the controller directly in-process.
 *   → Spring auto-creates it when you use @WebMvcTest.
 *
 * @MockBean CalculatorService
 *   → Creates a Mockito MOCK of CalculatorService and registers it
 *     as a Spring Bean — replacing any real CalculatorService.
 *   → The controller gets this mock injected instead of the real service.
 *   → NOTE: Use @MockBean (not @MockitoBean) in Spring Boot 3.2.5.
 *
 * Key Mockito Methods:
 * ─────────────────────────────────────────────────────────────
 * when(mock.method(args)).thenReturn(value)
 *   → "When this method is called with these args, return this value."
 *   → Example: when(calculatorService.add(10, 20)).thenReturn(30)
 *
 * when(mock.method(args)).thenThrow(exception)
 *   → "When this method is called, throw this exception instead."
 *   → Example: when(calculatorService.add(-1, 5))
 *              .thenThrow(new IllegalArgumentException("..."))
 *
 * verify(mock).method(args)
 *   → Asserts that the method was called (optional but good practice).
 *
 * Key MockMvc Methods:
 * ─────────────────────────────────────────────────────────────
 * mockMvc.perform(get("/url?params"))
 *   → Sends a simulated GET request to the given URL.
 *
 * .andExpect(status().isOk())
 *   → Asserts the HTTP response status is 200 OK.
 *
 * .andExpect(status().isBadRequest())
 *   → Asserts the HTTP response status is 400 Bad Request.
 *
 * .andExpect(content().string("..."))
 *   → Asserts the response body is exactly this string.
 *
 * .andExpect(content().string(containsString("...")))
 *   → Asserts the response body CONTAINS this substring.
 */
@WebMvcTest(CalculatorController.class)
class CalculatorControllerTest {

    // MockMvc simulates HTTP requests — no real server started
    @Autowired
    private MockMvc mockMvc;

    // @MockBean creates a Mockito mock AND registers it as a Spring bean
    // The controller will receive this mock instead of the real service
    @MockBean
    private CalculatorService calculatorService;

    // ─────────────────────────────────────────────────────────────
    //  TEST 1: Normal addition — GET /calculator/add?a=10&b=20 → 200 OK, "30"
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("GET /calculator/add?a=10&b=20 → 200 OK, result 30")
    void testAdd_NormalValues_Returns200WithResult() throws Exception {

        // ARRANGE: Tell Mockito what to return when service.add(10, 20) is called
        when(calculatorService.add(10, 20)).thenReturn(30);

        // ACT + ASSERT: Perform the GET request and check the response
        mockMvc.perform(get("/calculator/add")
                    .param("a", "10")   // adds ?a=10 to the URL
                    .param("b", "20"))  // adds &b=20 to the URL
               .andExpect(status().isOk())                           // HTTP 200
               .andExpect(content().string("Result: 10 + 20 = 30")); // response body

        // VERIFY: confirm the service was called exactly once with (10, 20)
        verify(calculatorService, times(1)).add(10, 20);
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 2: Addition with zero — GET /calculator/add?a=5&b=0
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("GET /calculator/add?a=5&b=0 → 200 OK, result 5")
    void testAdd_WithZero_Returns200() throws Exception {

        when(calculatorService.add(5, 0)).thenReturn(5);

        mockMvc.perform(get("/calculator/add")
                    .param("a", "5")
                    .param("b", "0"))
               .andExpect(status().isOk())
               .andExpect(content().string("Result: 5 + 0 = 5"));
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 3: Negative first parameter — should return 400
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("GET /calculator/add?a=-1&b=5 → 400 Bad Request")
    void testAdd_NegativeFirstParam_Returns400() throws Exception {

        // ARRANGE: Mock the service to throw an exception for negative input
        when(calculatorService.add(-1, 5))
            .thenThrow(new IllegalArgumentException(
                "Parameters must be non-negative. Received: a=-1, b=5"));

        // ACT + ASSERT
        mockMvc.perform(get("/calculator/add")
                    .param("a", "-1")
                    .param("b", "5"))
               .andExpect(status().isBadRequest()); // HTTP 400
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 4: Negative second parameter — should return 400
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("GET /calculator/add?a=5&b=-3 → 400 Bad Request")
    void testAdd_NegativeSecondParam_Returns400() throws Exception {

        when(calculatorService.add(5, -3))
            .thenThrow(new IllegalArgumentException(
                "Parameters must be non-negative. Received: a=5, b=-3"));

        mockMvc.perform(get("/calculator/add")
                    .param("a", "5")
                    .param("b", "-3"))
               .andExpect(status().isBadRequest()); // HTTP 400
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 5: Missing parameter — only 'a' provided, 'b' missing
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("GET /calculator/add?a=10 (b missing) → 400 Bad Request")
    void testAdd_MissingParameter_Returns400() throws Exception {

        // Spring itself throws MissingServletRequestParameterException
        // before even reaching the service — no Mockito setup needed
        mockMvc.perform(get("/calculator/add")
                    .param("a", "10"))
               // No "b" param — Spring throws MissingServletRequestParameterException
               // which our @ExceptionHandler catches and returns 400
               .andExpect(status().isBadRequest());
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 6: Both parameters missing
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("GET /calculator/add (no params) → 400 Bad Request")
    void testAdd_NoParameters_Returns400() throws Exception {

        mockMvc.perform(get("/calculator/add"))
               .andExpect(status().isBadRequest());
    }

    // ─────────────────────────────────────────────────────────────
    //  TEST 7: Verify error body for negative parameter
    // ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Negative param response body contains 'Error'")
    void testAdd_NegativeParam_ResponseBodyContainsError() throws Exception {

        when(calculatorService.add(-5, 10))
            .thenThrow(new IllegalArgumentException(
                "Parameters must be non-negative. Received: a=-5, b=10"));

        mockMvc.perform(get("/calculator/add")
                    .param("a", "-5")
                    .param("b", "10"))
               .andExpect(status().isBadRequest())
               // containsString (Hamcrest matcher) — partial match on response body
               .andExpect(content().string(
                   org.hamcrest.Matchers.containsString("Error")));
    }
}
