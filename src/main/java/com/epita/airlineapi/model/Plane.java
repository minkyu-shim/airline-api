package com.epita.airlineapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "planes")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Plane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planeId;

    @Column(nullable = false)
    private String planeBrand;

    @Column(nullable = false)
    private String planeModel;

    private Integer manufacturingYear;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        // REFACTOR: Using Pattern Matching for instanceof
        if (!(o instanceof Plane other)) return false;

        // Handle Hibernate Proxies (lazy loading wrappers)
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();

        if (thisEffectiveClass != oEffectiveClass) return false;

        // Compare IDs safely using getters
        return getPlaneId() != null && Objects.equals(getPlaneId(), other.getPlaneId());
    }

    @Override
    public final int hashCode() {
        // Return a constant hash for JPA entity consistency
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}