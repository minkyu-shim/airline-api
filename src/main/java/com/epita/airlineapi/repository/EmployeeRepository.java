package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Find by the unique Business Key (Employee Number)
    Optional<Employee> findByEmployeeNumber(Long employeeNumber);

    // Check existence by business key
    boolean existsByEmployeeNumber(Long employeeNumber);

    // Check email uniqueness (Inherited from User)
    boolean existsByEmail(String email);

    // Delete by Business Key
    void deleteByEmployeeNumber(Long employeeNumber);
}
