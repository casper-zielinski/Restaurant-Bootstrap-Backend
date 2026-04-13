package com.tavernaluna.backend.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tavernaluna.backend.Constants.CookieConstants;
import com.tavernaluna.backend.Reservation;
import com.tavernaluna.backend.ReservationController;
import com.tavernaluna.backend.ReservationService;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    private final String randomId = "121323-232323";
    private final String randomUserId = "user-123";

    @Test
    @DisplayName("Getting all Reservations a specific user has without userId, with wrong userId and correct userId")
    void testGetUserReservations() throws Exception {
        List<Reservation> mockData = ReservationTestFactory.getReservations(randomUserId);

        when(reservationService.getReservationsByUserId(randomUserId)).thenReturn(mockData);

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("No user session found"));

        mockMvc.perform(get("/reservations").cookie(new Cookie(CookieConstants.userId, "randomUserId")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully fetched User Reservations"));

        mockMvc.perform(get("/reservations").cookie(new Cookie(CookieConstants.userId, randomUserId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully fetched User Reservations"))
                .andExpect(jsonPath("$.data.length()").value(4))
                .andExpect(jsonPath("$.data[0].id").value(mockData.getFirst().getId()))
                .andExpect(jsonPath("$.data[1].name").value(mockData.get(1).getName()))
                .andExpect(jsonPath("$.data[2].time").value(Matchers.startsWith("00:00")))
                .andExpect(jsonPath("$.data[3].phoneNumber").value("+43 343435 3553434 434"))
                .andExpect(jsonPath("$.data[3].price").value(12.22))
                .andExpect(jsonPath("$.data[0].date").value(LocalDate.EPOCH.toString()));
    }

    @Test
    @DisplayName("Getting Reservation by Id without Cookie, with wrong Cookie, and with correct Cookie")
    void testGetReservationById() throws Exception {
        Reservation reservation = ReservationTestFactory.create(randomId, randomUserId);

        when(reservationService.getReservationsById(randomId, randomUserId)).thenReturn(Optional.of(reservation));

        mockMvc.perform(get("/reservations/" + randomId))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/reservations/" + randomId).cookie(new Cookie(CookieConstants.userId, "anotherRandomUserId")))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/reservations/" + "randomId").cookie(new Cookie(CookieConstants.userId, randomUserId)))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/reservations/" + randomId).cookie(new Cookie(CookieConstants.userId, randomUserId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(String.format("Successfully fetched reservation with id:%s", randomId)))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(randomId))
                .andExpect(jsonPath("$.data.name").value(reservation.getName()))
                .andExpect(jsonPath("$.data.price").value(reservation.getPrice()))
                .andExpect(jsonPath("$.data.phoneNumber").value(reservation.getPhoneNumber()))
                .andExpect(jsonPath("$.data.date").value(reservation.getDate().toString()))
                .andExpect(jsonPath("$.data.time").value(Matchers.startsWith("00:00")))
                .andExpect(jsonPath("$.data.userId").value(randomUserId));
    }

    // userId cannot be tested since service is mocked
    @Test
    @DisplayName("Creating Reservation without userId")
    void testCreateReservationWithoutUserId() throws Exception {
        Reservation reservation = ReservationTestFactory.create(randomId, null);

        when(reservationService.createReservation(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isCreated())
                .andExpect(cookie().exists(CookieConstants.userId))
                .andExpect(jsonPath("$.message").value("Successfully created new reservation"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(randomId))
                .andExpect(jsonPath("$.data.name").value(reservation.getName()))
                .andExpect(jsonPath("$.data.price").value(reservation.getPrice()))
                .andExpect(jsonPath("$.data.phoneNumber").value(reservation.getPhoneNumber()))
                .andExpect(jsonPath("$.data.date").value(reservation.getDate().toString()))
                .andExpect(jsonPath("$.data.time").value(Matchers.startsWith("00:00")));
    }

    @Test
    @DisplayName("Creating Reservation with a existing userId")
    void testCreateReservationWithUserId() throws Exception {
        Reservation reservation = ReservationTestFactory.create(randomId, randomUserId);

        when(reservationService.createReservation(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/reservations")
                        .cookie(new Cookie(CookieConstants.userId, randomUserId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created new reservation"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(randomId))
                .andExpect(jsonPath("$.data.name").value(reservation.getName()))
                .andExpect(jsonPath("$.data.price").value(reservation.getPrice()))
                .andExpect(jsonPath("$.data.phoneNumber").value(reservation.getPhoneNumber()))
                .andExpect(jsonPath("$.data.date").value(reservation.getDate().toString()))
                .andExpect(jsonPath("$.data.time").value(Matchers.startsWith("00:00")))
                .andExpect(jsonPath("$.data.userId").value(randomUserId));
    }
}
