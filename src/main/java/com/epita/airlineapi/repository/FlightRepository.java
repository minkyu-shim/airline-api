package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
