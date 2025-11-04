package com.tavernaluna.backend;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Reservation entities in the database.
 * Provides CRUD operations through Spring Data JPA.
 */
public interface ReservationRepository extends
        JpaRepository<Reservation, String> {

}
