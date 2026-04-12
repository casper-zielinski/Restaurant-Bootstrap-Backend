package com.tavernaluna.backend;

import com.tavernaluna.backend.Constants.CookieConstants;
import com.tavernaluna.backend.DTO.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<ApiResponse<List<Reservation>>> getAllUserReservations(@CookieValue(value = "userId", required = false) String userId) {
        try {
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<List<Reservation>>error("No user session found", null));
            }
            List<Reservation> userReservations = service.getReservationsByUserId(userId);
            return ResponseEntity.ok(ApiResponse.ok("Successfully fetched User Reservations", userReservations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<List<Reservation>>error("Error fetching User Reservations", null, e.getMessage()));
        }
    }

    /**
     * Retrieves a specific reservation by its ID.
     *
     * @param id The reservation ID to search for
     * @return The reservation if found, otherwise 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Reservation>> getOneReservation(@PathVariable String id) {
        try {
            Optional<Reservation> reservation = service.getReservationsById(id);
            if (reservation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<Reservation>error(String.format("Reservation with id:%s does not exist", id), null));
            } else {
                return ResponseEntity.ok(ApiResponse.ok(String.format("Successfully fetched reservation with id:%s", id), reservation.get()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<Reservation>error(String.format("Error fetching Reservation with id:%s", id), null, e.getMessage()));
        }

    }

    /**
     * Creates a new reservation in the database.
     *
     * @param reservation The reservation data to save
     * @return The saved reservation, or error message on failure
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Reservation>> addReservation(@Valid @RequestBody Reservation reservation, HttpServletResponse response, @CookieValue(value = "userId", required = false) String userId) {
        try {
            if (userId == null) {
                String id = UUID.randomUUID().toString();
                Cookie cookie = new Cookie(CookieConstants.userId, id);
                reservation.setUserId(id);
                response.addCookie(cookie);
            } else {
                reservation.setUserId(userId);
            }
            Reservation createdReservation = service.createReservation(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Successfully created new reservation", createdReservation));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<Reservation>error("Error creating new reservation", null, e.getMessage()));
        }
    }
}
