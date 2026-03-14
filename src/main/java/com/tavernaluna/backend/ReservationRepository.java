package com.tavernaluna.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends
        JpaRepository<Reservation, String> {
    List<Reservation> findByName(String name);
}
