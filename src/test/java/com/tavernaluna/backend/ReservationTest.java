package com.tavernaluna.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class ReservationTest {

    @Autowired
    private JacksonTester<Reservation> json;

    @Test
    void reservationSerializationTest() throws IOException {
        Reservation reservation = new Reservation("r5", LocalTime.MIN, "Casper Zielinski", LocalDate.EPOCH, null, "+43 343435 3553434 434", 12.22, List.of(true, true, false));
        assertThat(json.write(reservation)).isStrictlyEqualToJson("expected.json");
        assertThat(json.write(reservation)).hasJsonPathArrayValue("@.tables");
    }
}
