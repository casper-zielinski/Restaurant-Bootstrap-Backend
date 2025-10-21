package com.tavernaluna.backend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends
        JpaRepository<Reservation, String> {
}
