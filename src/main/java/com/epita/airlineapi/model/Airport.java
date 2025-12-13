package com.epita.airlineapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "airports")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airportId;

    private String airportName;

    private String airportCountry;

    private String airportCity;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        // REFACTOR: Using Pattern Matching for instanceof
        if (!(o instanceof Airport other)) return false;

        // Handle Hibernate Proxies (lazy loading wrappers)
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();

        if (thisEffectiveClass != oEffectiveClass) return false;

        // Compare IDs safely using getters
        return getAirportId() != null && Objects.equals(getAirportId(), other.getAirportId());
    }

    @Override
    public final int hashCode() {
        // Return a constant hash for JPA entity consistency
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}