package com.tavernaluna.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main application class for the Taverna Luna restaurant backend.
 * This serves as the entry point for the Spring Boot application.
 */
@SpringBootApplication
@RestController
public class BackendApplication {

    /**
     * Application entry point that starts the Spring Boot application.
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
