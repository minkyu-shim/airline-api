package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Airport;
import com.epita.airlineapi.repository.AirportRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AirportService {
    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<Airport> getAirports() {
        return airportRepository.findAll();
    }

    public Airport saveAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    public Optional<Airport> getAirportById(Long airportId) {
        return airportRepository.findById(airportId);
    }

    public void deleteAirport(Long airportId) {
        boolean exists = airportRepository.existsById(airportId);

        if (!exists) {
            throw new IllegalStateException("Airport with id " + airportId + " does not exist");
        }

        airportRepository.deleteById(airportId);
    }

    @Transactional
    public void updateAirport(Long airportId, Airport updateRequest) {
        // 1. Retrieve the existing airport
        Airport airport = airportRepository.findById(airportId)
                .orElseThrow(() -> new IllegalStateException("Airport with id " + airportId + " does not exist"));

        // 2. Update Airport Name
        if (updateRequest.getAirportName() != null &&
                !updateRequest.getAirportName().isEmpty() &&
                !Objects.equals(airport.getAirportName(), updateRequest.getAirportName())) {
            airport.setAirportName(updateRequest.getAirportName());
        }

        // 3. Update Airport Country
        if (updateRequest.getAirportCountry() != null &&
                !updateRequest.getAirportCountry().isEmpty() &&
                !Objects.equals(airport.getAirportCountry(), updateRequest.getAirportCountry())) {
            airport.setAirportCountry(updateRequest.getAirportCountry());
        }

        // 4. Update Airport City
        if (updateRequest.getAirportCity() != null &&
                !updateRequest.getAirportCity().isEmpty() &&
                !Objects.equals(airport.getAirportCity(), updateRequest.getAirportCity())) {
            airport.setAirportCity(updateRequest.getAirportCity());
        }
    }
}
