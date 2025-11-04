package com.tavernaluna.backend;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * Entity class representing a restaurant reservation.
 * Stores reservation details including time, date, price, and table assignments.
 */
@Entity
public class Reservation {
    @Id
    @NotBlank(message = "ID darf nicht leer sein")
    private String id;

    @NotBlank(message = "Zeit darf nicht leer sein")
    private LocalTime time;

    @NotBlank(message = "Datum darf nicht leer sein")
    private LocalDate date;

    @NotNull(message = "Preis darf nicht null sein")
    @Min(value = 0, message = "Preis muss positiv sein")
    @Size(min = 1, message = "Mindestens ein Tisch muss ausgewählt werden")
    private Integer price;

    // Stored as JSON to allow frontend to send array and store as list in backend
    @NotNull(message = "Tische dürfen nicht null sein")
    @Column(columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Boolean> tables;

    /** Default constructor required by JPA */
    public Reservation() {}

    public Reservation(String id, LocalTime time, LocalDate date, Integer price, List<Boolean> tables) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.price = price;
        this.tables = tables;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<Boolean> getTables() {
        return tables;
    }

    public void setTables(List<Boolean> tables) {
        this.tables = tables;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(time, that.time) && Objects.equals(date, that.date) &&
               Objects.equals(price, that.price) && Objects.equals(tables, that.tables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, date, price, tables);
    }
}
