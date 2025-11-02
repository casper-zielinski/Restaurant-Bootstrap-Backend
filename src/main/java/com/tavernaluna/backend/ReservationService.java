package com.tavernaluna.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing restaurant reservations.
 * Provides API endpoints for retrieving and creating reservations.
 */
@RestController
@RequestMapping("api/Reservations")
@CrossOrigin(origins =
        {
        "http://localhost:3000",
        "https://restaurant-bootstrap-gamma.vercel.app"
        }) // Allow requests from frontend
public class ReservationService {

    private ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all reservations from the database.
     * @return List of all reservations
     */
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(repository.findAll());
    }

    /**
     * Retrieves a specific reservation by its ID.
     * @param id The reservation ID to search for
     * @return The reservation if found, otherwise 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservations(@PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new reservation in the database.
     * @param reservation The reservation data to save
     * @return The saved reservation, or error message on failure
     */
    @PostMapping
    public ResponseEntity<?> addReservation(@RequestBody Reservation reservation) {
        try {
            return ResponseEntity.ok(repository.save(reservation));
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
