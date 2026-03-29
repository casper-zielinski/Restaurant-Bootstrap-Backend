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
    private final String testFile = "expected.json";
    private final String testFileNoUserID = "noUserId.json";


    @Autowired
    private JacksonTester<Reservation> json;

    @Test
    void reservationSerializationTest() throws IOException {
        Reservation reservation = new Reservation("r5", LocalTime.MIN, "Casper Zielinski", LocalDate.EPOCH, null, "+43 343435 3553434 434", 12.22, List.of(true, true, false), "123-345");
        assertThat(json.write(reservation)).isStrictlyEqualToJson(testFile);
        assertThat(json.write(reservation)).hasJsonPathArrayValue("@.tables");
        assertThat(json.write(reservation)).hasJsonPathValue("$.id", reservation.getId());
        assertThat(json.write(reservation)).hasJsonPathValue("$.userId", reservation.getUserId());
        assertThat(json.write(reservation)).extractingJsonPathValue("$.email").isNull();
        assertThat(json.write(reservation)).hasJsonPathNumberValue("$.price", reservation.getPrice());
        assertThat(json.write(reservation)).doesNotHaveJsonPath("$.IBAN");
    }

    @Test
    void reservationSerializationTestWithNoUserID() throws IOException {
        Reservation reservation = new Reservation("r5", LocalTime.MIN, "Casper Zielinski", LocalDate.EPOCH, null, "+43 343435 3553434 434", 12.22, List.of(true, true, false), null);
        assertThat(json.write(reservation)).isStrictlyEqualToJson(testFileNoUserID);
        assertThat(json.write(reservation)).hasJsonPathArrayValue("@.tables");
        assertThat(json.write(reservation)).hasJsonPathValue("$.id", reservation.getId());
        assertThat(json.write(reservation)).extractingJsonPathValue("$.userId").isNull();
        assertThat(json.write(reservation)).extractingJsonPathValue("$.email").isNull();
        assertThat(json.write(reservation)).hasJsonPathNumberValue("$.price", reservation.getPrice());
        assertThat(json.write(reservation)).doesNotHaveJsonPath("$.IBAN");
    }

    @Test
    void reservationDeserializationTest() throws Exception {
        Reservation reservation = json.readObject(testFile);
        assertThat(reservation.getEmail()).isNull();
        assertThat(reservation.getId()).isEqualTo("r5");
        assertThat(reservation.getTime()).isEqualTo(LocalTime.MIN);
    }


    @Test
    void reservationDeserializationTestWithNoUserId() throws Exception {
        Reservation reservation = json.readObject(testFileNoUserID);
        assertThat(reservation.getEmail()).isNull();
        assertThat(reservation.getId()).isEqualTo("r5");
        assertThat(reservation.getTime()).isEqualTo(LocalTime.MIN);
        assertThat(reservation.getUserId()).isNull();
    }
}
