package com.epita.airlineapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
    @Column(name = "flight_id")
    private Long flightId;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "departure_city", nullable = false)
    private String departureCity;

    @Column(name = "arrival_city", nullable = false)
    private String arrivalCity;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "arrival_date", nullable = false)
    private LocalDate arrivalDate;

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

    @Column(name = "number_of_seats", nullable = false)
    private Integer numberOfSeats;

    @Column(name = "business_price", nullable = false)
    private BigDecimal businessPrice;

    @Column(name = "economy_price", nullable = false)
    private BigDecimal economyPrice;

    // Foreign Key mapping
    // orphanRemoval : The reward still exists in the MilesReward table. It is just "unlinked"
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<MilesReward> rewards;

    // Foreign key mapping
    // orphanRemoval : The booking still exists in the Book table. It is just "unlinked"
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<Book> bookings;


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