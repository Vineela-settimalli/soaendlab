package com.exam.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CalculatorApplication — Entry Point
 *
 * @SpringBootApplication is a shortcut for three annotations:
 *   1. @Configuration    — marks this as a source of Spring Bean definitions
 *   2. @EnableAutoConfiguration — tells Spring Boot to auto-configure based on
 *                                 the JARs on the classpath (e.g., Tomcat, Jackson)
 *   3. @ComponentScan   — scans this package and sub-packages for @Component,
 *                         @Service, @RestController, etc.
 *
 * SpringApplication.run() bootstraps the entire application and starts
 * the embedded Tomcat web server on port 8080.
 */
@SpringBootApplication
public class CalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalculatorApplication.class, args);
    }
}
