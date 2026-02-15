package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.Book;
import com.epita.airlineapi.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Counts how many bookings a specific client has in a specific year
    // Assumes your Book entity has a 'flight' which has a 'departureDate'
    @Query("SELECT COUNT(b) FROM Book b WHERE b.client = :client AND YEAR(b.flight.departureDate) = :year")
    long countFlightsByClientAndYear(@Param("client") Client client, @Param("year") int year);

    long countByFlight_FlightId(Long flightId);

    boolean existsByFlight_FlightIdAndClient_UserId(Long flightId, Long clientId);
}
