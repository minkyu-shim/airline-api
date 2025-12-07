package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {
}
