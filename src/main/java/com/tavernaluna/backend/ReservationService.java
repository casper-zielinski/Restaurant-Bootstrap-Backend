package com.tavernaluna.backend;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business Logik
 */
@Service
public class ReservationService {

    private final  ReservationRepository repository;
    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public List<Reservation> getAllReservations() {
        return repository.findAll();
    }

    public Optional<Reservation> getReservationById(String id) {
        return repository.findById(id);
    }

    public Reservation createReservation(Reservation reservation) {
        return repository.save(reservation);
    }
}
