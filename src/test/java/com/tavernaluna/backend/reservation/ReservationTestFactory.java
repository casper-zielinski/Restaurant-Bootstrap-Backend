package com.tavernaluna.backend.reservation;

import com.tavernaluna.backend.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationTestFactory {

    public static List<Reservation> getReservations(String userId) {
        return List.of(
                create("r5", userId),
                create("r6", userId),
                create("r7", userId),
                create("r8", userId)
        );
    }

    public static Reservation create(String id, String userId) {
        return new Reservation(
                id, LocalTime.MIN, "Casper Zielinski", LocalDate.EPOCH,
                null, "+43 343435 3553434 434", 12.22,
                List.of(true, true, false), userId
        );
    }
}