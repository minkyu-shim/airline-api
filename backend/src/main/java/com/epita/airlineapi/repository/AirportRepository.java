package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    // Finds airport by city (e.g., "Paris", "London")
    Optional<Airport> findByAirportCity(String airportCity);
}
