package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Flight;
import com.epita.airlineapi.repository.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> getFlightById(Long flightId) {
        return flightRepository.findById(flightId);
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public void deleteFlight(Long flightId) {
        boolean exists = flightRepository.existsById(flightId);

        if (!exists) {
            throw new EntityNotFoundException("Flight with id " + flightId + " does not exist");
        }

        flightRepository.deleteById(flightId);
    }

    @Transactional
    public void updateFlight(Long flightId, Flight updateRequest) {

        // 1. Retrieve existing Flight
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalStateException(
                        "Flight with id " + flightId + " does not exist"
                ));

        // 2. Update Flight Number
        if (updateRequest.getFlightNumber() != null &&
                !updateRequest.getFlightNumber().isEmpty() &&
                !Objects.equals(flight.getFlightNumber(), updateRequest.getFlightNumber())) {
            flight.setFlightNumber(updateRequest.getFlightNumber());
        }

        // 3. Update Departure City
        if (updateRequest.getDepartureCity() != null &&
                !updateRequest.getDepartureCity().isEmpty() &&
                !Objects.equals(flight.getDepartureCity(), updateRequest.getDepartureCity())) {
            flight.setDepartureCity(updateRequest.getDepartureCity());
        }

        // 4. Update Arrival City
        if (updateRequest.getArrivalCity() != null &&
                !updateRequest.getArrivalCity().isEmpty() &&
                !Objects.equals(flight.getArrivalCity(), updateRequest.getArrivalCity())) {
            flight.setArrivalCity(updateRequest.getArrivalCity());
        }

        // 5. Update Departure Date
        if (updateRequest.getDepartureDate() != null &&
                !Objects.equals(flight.getDepartureDate(), updateRequest.getDepartureDate())) {
            flight.setDepartureDate(updateRequest.getDepartureDate());
        }

        // 6. Update Arrival Date
        if (updateRequest.getArrivalDate() != null &&
                !Objects.equals(flight.getArrivalDate(), updateRequest.getArrivalDate())) {
            flight.setArrivalDate(updateRequest.getArrivalDate());
        }

        // 7. Update Departure Airport
        if (updateRequest.getDepartureAirport() != null &&
                !updateRequest.getDepartureAirport().isEmpty() &&
                !Objects.equals(flight.getDepartureAirport(), updateRequest.getDepartureAirport())) {
            flight.setDepartureAirport(updateRequest.getDepartureAirport());
        }

        // 8. Update Arrival Airport
        if (updateRequest.getArrivalAirport() != null &&
                !updateRequest.getArrivalAirport().isEmpty() &&
                !Objects.equals(flight.getArrivalAirport(), updateRequest.getArrivalAirport())) {
            flight.setArrivalAirport(updateRequest.getArrivalAirport());
        }

        // 9. Update Plane (IMPORTANT: Object Relation, Not ID)
        if (updateRequest.getPlane() != null &&
                !Objects.equals(flight.getPlane(), updateRequest.getPlane())) {
            flight.setPlane(updateRequest.getPlane());
        }

        // 10. Update Number of Seats
        if (updateRequest.getNumberOfSeats() != null &&
                !Objects.equals(flight.getNumberOfSeats(), updateRequest.getNumberOfSeats())) {
            flight.setNumberOfSeats(updateRequest.getNumberOfSeats());
        }

        // 11. Update Business Price
        if (updateRequest.getBusinessPrice() != null &&
                !Objects.equals(flight.getBusinessPrice(), updateRequest.getBusinessPrice())) {
            flight.setBusinessPrice(updateRequest.getBusinessPrice());
        }

        // 12. Update Economy Price
        if (updateRequest.getEconomyPrice() != null &&
                !Objects.equals(flight.getEconomyPrice(), updateRequest.getEconomyPrice())) {
            flight.setEconomyPrice(updateRequest.getEconomyPrice());
        }
    }

}
