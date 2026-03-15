package com.tavernaluna.backend;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing restaurant reservations.
 * Provides API endpoints for retrieving and creating reservations.
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    /**
     * Retrieves all reservations from the database.
     *
     * @return List of all reservations
     */
    @GetMapping
    public ResponseEntity<List<@Valid Reservation>> getAllReservations(@CookieValue(value = "userId") String userId) {
        return ResponseEntity.ok(service.getReservationsByUserId(userId));
    }

    /**
     * Retrieves a specific reservation by its ID.
     *
     * @param id The reservation ID to search for
     * @return The reservation if found, otherwise 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservations(@PathVariable String id) {
        return service.getReservationsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new reservation in the database.
     *
     * @param reservation The reservation data to save
     * @return The saved reservation, or error message on failure
     */
    @PostMapping
    public ResponseEntity<?> addReservation(@Valid @RequestBody Reservation reservation, HttpServletResponse response, @CookieValue(value = "userId", required = false) String userId) {
        try {
            if (userId == null) {
                String id = UUID.randomUUID().toString();
                Cookie cookie = new Cookie("userId", id);
                reservation.setUserId(id);
                response.addCookie(cookie);
            } else {
                reservation.setUserId(userId);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(service.createReservation(reservation));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
