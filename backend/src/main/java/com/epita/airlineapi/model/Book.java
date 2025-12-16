package com.epita.airlineapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "type_of_seat")
    private String typeOfSeat;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Check memory address

        // Use instanceof to handle Hibernate Proxies correctly
        if (!(o instanceof Book)) return false;

        Book book = (Book) o;

        // Use Getters! Direct field access (book.reservationId) fails on proxies
        // Handle null IDs safely
        return getReservationId() != null && getReservationId().equals(book.getReservationId());
    }

    @Override
    public int hashCode() {
        // 5. Return a constant. This ensures the object doesn't "disappear"
        // from a HashSet if the ID changes from null to 1 after saving.
        return getClass().hashCode();
    }
}
