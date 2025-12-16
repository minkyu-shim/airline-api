package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    // Find by business key (e.g. "AF123")
    Optional<Flight> findByFlightNumber(String flightNumber);

    // Check existence by business key
    boolean existsByFlightNumber(String flightNumber);

    List<Flight> findByDepartureCityIgnoreCaseAndArrivalCityIgnoreCaseAndDepartureDate(String departureCity, String arrivalCity, LocalDate date);



    // OLD (Deleted):
    // ...DepartureDateBetween(String dep, String arr, LocalDateTime start, LocalDateTime end);


}
