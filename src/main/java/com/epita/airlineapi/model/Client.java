package com.epita.airlineapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true) // Prints User fields (Name, Email) too
@SuperBuilder  // Allows building Client + User fields together
@PrimaryKeyJoinColumn(name = "user_id") // Ensures the DB foreign key is named "user_id"
public class Client extends User {
    @Column(name = "passport_number", nullable = false, unique = true)
    private String passportNumber;

    // Foreign key mapping
    // orphanRemoval : The reward still exists in the MilesReward table. It is just "unlinked"
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude  // Critical to prevent Client -> MilesReward -> Client infinite loop / StackOverflowError
    @JsonIgnore
    private List<MilesReward> rewards;

    // Foreign key mapping
    // orphanRemoval : The booking still exists in the Book table. It is just "unlinked"
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<Book> bookings;

    @Column(name = "discount_code")
    private String discountCode;
}
