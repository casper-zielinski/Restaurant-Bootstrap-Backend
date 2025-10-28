package com.tavernaluna.backend;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Objects;

/**
 * Entity class representing a restaurant reservation.
 * Stores reservation details including time, date, price, and table assignments.
 */
@Entity
public class Reservation {
    @Id private String id;
    private String time;
    private String date;
    private Integer price;
    // Stored as JSON to allow frontend to send array and store as list in backend
    @Column(columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Boolean> tables;

    /** Default constructor required by JPA */
    public Reservation() {}

    public Reservation(String id, String time, String date, Integer price, List<Boolean> tables) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
