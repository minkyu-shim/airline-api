package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Airport;
import com.epita.airlineapi.model.Flight;
import com.epita.airlineapi.model.Plane;
import com.epita.airlineapi.repository.AirportRepository;
import com.epita.airlineapi.repository.FlightRepository;
import com.epita.airlineapi.repository.PlaneRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final PlaneRepository planeRepository;

    public FlightService(FlightRepository flightRepository, AirportRepository airportRepository, PlaneRepository planeRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.planeRepository = planeRepository;
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

    public List<Flight> searchFlights(String departureCity, String arrivalCity, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);
        return flightRepository.findByDepartureCityIgnoreCaseAndArrivalCityIgnoreCaseAndDepartureDateBetween(
                departureCity,
                arrivalCity,
                startOfDay,
                endOfDay
        );
    }

    // CREATE
    @Transactional
    public Flight saveFlight(Flight flight) {
        if (flight == null) {
            throw new IllegalArgumentException("Flight payload is required");
        }

        if (flight.getFlightNumber() == null || flight.getFlightNumber().isBlank()) {
            throw new IllegalArgumentException("Flight number is required");
        }

        if (flightRepository.existsByFlightNumber(flight.getFlightNumber())) {
            throw new IllegalStateException("Flight number " + flight.getFlightNumber() + " already exists");
        }

        resolveAndAssignFlightRelations(flight);
        validateSeatAndPrices(flight);
        validateFlightDates(flight);

        return flightRepository.save(flight);
    }

    // UPDATE
    @Transactional
    public Flight updateFlight(Long flightId, Flight updateRequest) {
        if (updateRequest == null) {
            throw new IllegalArgumentException("Flight update payload is required");
        }

        Flight flight = getFlightById(flightId);

        // 1.Update Flight Number
        if (updateRequest.getFlightNumber() != null && !updateRequest.getFlightNumber().isBlank()) {
            if (!updateRequest.getFlightNumber().equals(flight.getFlightNumber())) {
                if (flightRepository.existsByFlightNumber(updateRequest.getFlightNumber())) {
                    throw new IllegalStateException("Flight number " + updateRequest.getFlightNumber() + " already exists");
                }
                flight.setFlightNumber(updateRequest.getFlightNumber());
            }
        }

        // 2.Update Cities
        if (updateRequest.getDepartureCity() != null && !updateRequest.getDepartureCity().isBlank()) {
            flight.setDepartureCity(updateRequest.getDepartureCity());
        }
        if (updateRequest.getArrivalCity() != null && !updateRequest.getArrivalCity().isBlank()) {
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
        if (updateRequest.getDepartureAirport() != null) {
            flight.setDepartureAirport(resolveAirportById(updateRequest.getDepartureAirport().getAirportId(), "Departure airport"));
        }
        if (updateRequest.getArrivalAirport() != null) {
            flight.setArrivalAirport(resolveAirportById(updateRequest.getArrivalAirport().getAirportId(), "Arrival airport"));
        }
        if (updateRequest.getPlane() != null) {
            flight.setPlane(resolvePlaneById(updateRequest.getPlane().getPlaneId(), "Plane"));
        }

        // 5.Update Pricing/Seats
        if (updateRequest.getNumberOfSeats() != null) {
            if (updateRequest.getNumberOfSeats() <= 0) {
                throw new IllegalArgumentException("numberOfSeats must be greater than 0");
            }
            flight.setNumberOfSeats(updateRequest.getNumberOfSeats());
        }
        if (updateRequest.getBusinessPrice() != null) {
            if (updateRequest.getBusinessPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("businessPrice must be zero or positive");
            }
            flight.setBusinessPrice(updateRequest.getBusinessPrice());
        }
        if (updateRequest.getEconomyPrice() != null) {
            if (updateRequest.getEconomyPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("economyPrice must be zero or positive");
            }
            flight.setEconomyPrice(updateRequest.getEconomyPrice());
        }

        validateDistinctAirports(flight);
        return flight;
    }

    // DELETE
    public void deleteFlight(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new NoSuchElementException("Flight with id " + flightId + " not found");
        }
        flightRepository.deleteById(flightId);
    }

    private void resolveAndAssignFlightRelations(Flight flight) {
        if (flight.getDepartureAirport() == null || flight.getDepartureAirport().getAirportId() == null) {
            throw new IllegalArgumentException("Flight must have a departure airport");
        }

        if (flight.getArrivalAirport() == null || flight.getArrivalAirport().getAirportId() == null) {
            throw new IllegalArgumentException("Flight must have an arrival airport");
        }

        if (flight.getPlane() == null || flight.getPlane().getPlaneId() == null) {
            throw new IllegalArgumentException("Flight must have a plane");
        }

        flight.setDepartureAirport(resolveAirportById(flight.getDepartureAirport().getAirportId(), "Departure airport"));
        flight.setArrivalAirport(resolveAirportById(flight.getArrivalAirport().getAirportId(), "Arrival airport"));
        flight.setPlane(resolvePlaneById(flight.getPlane().getPlaneId(), "Plane"));
        validateDistinctAirports(flight);
    }

    private Airport resolveAirportById(Long airportId, String label) {
        if (airportId == null) {
            throw new IllegalArgumentException(label + " id is required");
        }

        return airportRepository.findById(airportId)
                .orElseThrow(() -> new NoSuchElementException(label + " with id " + airportId + " not found"));
    }

    private Plane resolvePlaneById(Long planeId, String label) {
        if (planeId == null) {
            throw new IllegalArgumentException(label + " id is required");
        }

        return planeRepository.findById(planeId)
                .orElseThrow(() -> new NoSuchElementException(label + " with id " + planeId + " not found"));
    }

    private void validateDistinctAirports(Flight flight) {
        if (flight.getDepartureAirport() != null
                && flight.getArrivalAirport() != null
                && Objects.equals(flight.getDepartureAirport().getAirportId(), flight.getArrivalAirport().getAirportId())) {
            throw new IllegalArgumentException("Departure and arrival airports must be different");
        }
    }

    private void validateSeatAndPrices(Flight flight) {
        if (flight.getNumberOfSeats() == null || flight.getNumberOfSeats() <= 0) {
            throw new IllegalArgumentException("numberOfSeats must be greater than 0");
        }

        if (flight.getBusinessPrice() == null || flight.getBusinessPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("businessPrice must be zero or positive");
        }

        if (flight.getEconomyPrice() == null || flight.getEconomyPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("economyPrice must be zero or positive");
        }
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
