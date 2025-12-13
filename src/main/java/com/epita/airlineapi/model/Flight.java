package com.epita.airlineapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "flights")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    @Column(nullable = false, unique = true)
    private String flightNumber;

    @Column(nullable = false)
    private String departureCity;

    @Column(nullable = false)
    private String arrivalCity;

    @Column(nullable = false)
    private LocalDateTime departureDate;

    @Column(nullable = false)
    private LocalDateTime arrivalDate;

    @ManyToOne
    @JoinColumn(name = "departure_airport_id", nullable = false)
    @ToString.Exclude
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    @ToString.Exclude
    private Airport arrivalAirport;

    @ManyToOne
    @JoinColumn(name = "plane_id", nullable = false)
    @ToString.Exclude // Best Practice: Prevent lazy-loading triggers when printing logs
    private Plane plane;

    @Column(nullable = false)
    private Integer numberOfSeats;

    @Column(nullable = false)
    private BigDecimal businessPrice;

    @Column(nullable = false)
    private BigDecimal economyPrice;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        // REFACTOR: Using Pattern Matching for instanceof
        if (!(o instanceof Flight other)) return false;

        // Handle Hibernate Proxies (lazy loading wrappers)
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();

        if (thisEffectiveClass != oEffectiveClass) return false;

        // Compare IDs safely using getters
        return getFlightId() != null && Objects.equals(getFlightId(), other.getFlightId());
    }

    @Override
    public final int hashCode() {
        // Return a constant hash for JPA entity consistency
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}