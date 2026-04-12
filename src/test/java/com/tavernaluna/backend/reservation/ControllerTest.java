package com.tavernaluna.backend.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tavernaluna.backend.Constants.CookieConstants;
import com.tavernaluna.backend.Reservation;
import com.tavernaluna.backend.ReservationController;
import com.tavernaluna.backend.ReservationService;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Test
    void testGetUserReservations() throws Exception {
        String userId = "user-123";
        List<Reservation> mockData = ReservationTestFactory.getReservations(userId);

        when(reservationService.getReservationsByUserId(userId)).thenReturn(mockData);

        mockMvc.perform(get("/reservations").cookie(new Cookie("userId", userId)))
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

    // userId cannot be tested since service is mocked
    @Test
    void testCreateReservationWithoutUserId() throws Exception {
        Reservation reservation = ReservationTestFactory.create("121323-232323", null);

        when(reservationService.createReservation(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isCreated())
                .andExpect(cookie().exists(CookieConstants.userId))
                .andExpect(jsonPath("$.message").value("Successfully created new reservation"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(reservation.getId()))
                .andExpect(jsonPath("$.data.name").value(reservation.getName()))
                .andExpect(jsonPath("$.data.price").value(reservation.getPrice()))
                .andExpect(jsonPath("$.data.phoneNumber").value(reservation.getPhoneNumber()))
                .andExpect(jsonPath("$.data.date").value(reservation.getDate().toString()))
                .andExpect(jsonPath("$.data.time").value(Matchers.startsWith("00:00")));
    }
}
