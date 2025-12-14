package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    // Find by business key (e.g. "AF123")
    Optional<Flight> findByFlightNumber(String flightNumber);

    // Check existence by business key
    boolean existsByFlightNumber(String flightNumber);
}
