package com.epita.airlineapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true) // Print Name/Email from User
@SuperBuilder  // Allows building Client + User fields together
@PrimaryKeyJoinColumn(name = "user_id") // Explicitly link to User table
public class Employee extends User {
    @Column(name = "employee_number", nullable = false, unique = true)
    private Long employeeNumber;

    @Column(name = "profession")
    private String profession;

    @Column(name = "title")
    private String title;
}
