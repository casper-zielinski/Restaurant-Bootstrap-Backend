package com.tavernaluna.backend;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing restaurant reservations.
 * Provides API endpoints for retrieving and creating reservations.
 */
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins =
        {
        "http://localhost:3000",
        "https://restaurant-bootstrap-gamma.vercel.app"
        }) // Allow requests from frontend
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    /**
     * Retrieves all reservations from the database.
     * @return List of all reservations
     */
    @GetMapping
    public ResponseEntity<List<@Valid Reservation>> getAllReservations() {
        return ResponseEntity.ok(service.getAllReservations());
    }

    /**
     * Retrieves a specific reservation by its ID.
     * @param id The reservation ID to search for
     * @return The reservation if found, otherwise 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservations(@PathVariable String id) {
        return service.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new reservation in the database.
     * @param reservation The reservation data to save
     * @return The saved reservation, or error message on failure
     */
    @PostMapping
    public ResponseEntity<?> addReservation(@Valid @RequestBody Reservation reservation) {
        try {
            return ResponseEntity.ok(service.createReservation(reservation));
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
