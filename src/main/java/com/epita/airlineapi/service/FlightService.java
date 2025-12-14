package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Flight;
import com.epita.airlineapi.repository.FlightRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    // GET ALL
    public List<Flight> getFlights() {
        return flightRepository.findAll();
    }

    // GET ONE (By ID)
    public Flight getFlightById(Long flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new NoSuchElementException("Flight with id " + flightId + " not found"));
    }

    // GET ONE (By Flight Number)
    public Flight getFlightByNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new NoSuchElementException("Flight " + flightNumber + " not found"));
    }

    // CREATE
    public Flight saveFlight(Flight flight) {
        // 1.Check Unique Flight Number
        if (flightRepository.existsByFlightNumber(flight.getFlightNumber())) {
            throw new IllegalStateException("Flight number " + flight.getFlightNumber() + " already exists");
        }

        // 2.Validate Dates
        validateFlightDates(flight);

        return flightRepository.save(flight);
    }

    // UPDATE
    @Transactional
    public Flight updateFlight(Long flightId, Flight updateRequest) {
        Flight flight = getFlightById(flightId);

        // 1.Update Flight Number (Check uniqueness if changed)
        if (updateRequest.getFlightNumber() != null &&
                !updateRequest.getFlightNumber().isEmpty() &&
                !Objects.equals(flight.getFlightNumber(), updateRequest.getFlightNumber())) {

            if (flightRepository.existsByFlightNumber(updateRequest.getFlightNumber())) {
                throw new IllegalStateException("Flight number " + updateRequest.getFlightNumber() + " already exists");
            }
            flight.setFlightNumber(updateRequest.getFlightNumber());
        }

        // 2.Update Cities
        if (updateRequest.getDepartureCity() != null && !updateRequest.getDepartureCity().isEmpty()) {
            flight.setDepartureCity(updateRequest.getDepartureCity());
        }
        if (updateRequest.getArrivalCity() != null && !updateRequest.getArrivalCity().isEmpty()) {
            flight.setArrivalCity(updateRequest.getArrivalCity());
        }

        // 3.Update Dates & Re-validate
        boolean datesChanged = false;
        if (updateRequest.getDepartureDate() != null) {
            flight.setDepartureDate(updateRequest.getDepartureDate());
            datesChanged = true;
        }
        if (updateRequest.getArrivalDate() != null) {
            flight.setArrivalDate(updateRequest.getArrivalDate());
            datesChanged = true;
        }

        if (datesChanged) {
            validateFlightDates(flight);
        }

        // 4.Update Relationships (Airports & Plane)
        // Ideally, you should fetch these entities from their repositories to ensure they exist
        if (updateRequest.getDepartureAirport() != null) flight.setDepartureAirport(updateRequest.getDepartureAirport());
        if (updateRequest.getArrivalAirport() != null) flight.setArrivalAirport(updateRequest.getArrivalAirport());
        if (updateRequest.getPlane() != null) flight.setPlane(updateRequest.getPlane());

        // 5.Update Pricing/Seats
        if (updateRequest.getNumberOfSeats() != null) flight.setNumberOfSeats(updateRequest.getNumberOfSeats());
        if (updateRequest.getBusinessPrice() != null) flight.setBusinessPrice(updateRequest.getBusinessPrice());
        if (updateRequest.getEconomyPrice() != null) flight.setEconomyPrice(updateRequest.getEconomyPrice());

        return flight;
    }

    // DELETE
    public void deleteFlight(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new NoSuchElementException("Flight with id " + flightId + " not found");
        }
        flightRepository.deleteById(flightId);
    }

    // Helper Method for Validation
    private void validateFlightDates(Flight flight) {
        if (flight.getDepartureDate() != null && flight.getArrivalDate() != null) {
            if (flight.getArrivalDate().isBefore(flight.getDepartureDate())) {
                throw new IllegalStateException("Arrival date cannot be before departure date");
            }
        }
    }
}