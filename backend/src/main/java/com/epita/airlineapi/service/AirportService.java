package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Airport;
import com.epita.airlineapi.repository.AirportRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    // GET ALL
    public List<Airport> getAirports() {
        return airportRepository.findAll();
    }

    // GET ONE
    public Airport getAirportById(Long airportId) {
        return airportRepository.findById(airportId)
                .orElseThrow(() -> new NoSuchElementException("Airport with id " + airportId + " does not exist"));
    }

    // CREATE
    public Airport saveAirport(Airport airport) {
        // Optional: Check if an airport with the same name already exists?
        return airportRepository.save(airport);
    }

    // UPDATE
    @Transactional
    public Airport updateAirport(Long airportId, Airport updateRequest) {
        // 1. Fetch existing airport (throws exception if missing)
        Airport airport = getAirportById(airportId);

        // 2.Update Name
        if (updateRequest.getAirportName() != null &&
                !updateRequest.getAirportName().isEmpty() &&
                !Objects.equals(airport.getAirportName(), updateRequest.getAirportName())) {
            airport.setAirportName(updateRequest.getAirportName());
        }

        // 3.Update Country
        if (updateRequest.getAirportCountry() != null &&
                !updateRequest.getAirportCountry().isEmpty() &&
                !Objects.equals(airport.getAirportCountry(), updateRequest.getAirportCountry())) {
            airport.setAirportCountry(updateRequest.getAirportCountry());
        }

        // 4.Update City
        if (updateRequest.getAirportCity() != null &&
                !updateRequest.getAirportCity().isEmpty() &&
                !Objects.equals(airport.getAirportCity(), updateRequest.getAirportCity())) {
            airport.setAirportCity(updateRequest.getAirportCity());
        }

        // 5.Return updated entity
        return airport;
    }

    // DELETE
    public void deleteAirport(Long airportId) {
        if (!airportRepository.existsById(airportId)) {
            throw new NoSuchElementException("Airport with id " + airportId + " does not exist");
        }
        airportRepository.deleteById(airportId);
    }
}