package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Airport;
import com.epita.airlineapi.service.AirportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/airport")
public class AirportController {
    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public ResponseEntity<List<Airport>> getAirports() {
        List<Airport> airports = airportService.getAirports();

        if (airports.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(airports, HttpStatus.OK);
    }

    @GetMapping(path = "{airportId}")
    public ResponseEntity<Airport> getAirportById(@PathVariable Long airportId) {
        Optional<Airport> airport = airportService.getAirportById(airportId);

        return airport.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Airport> createAirport(@RequestBody Airport airport) {
        Airport createdAirport = airportService.saveAirport(airport);
        return new ResponseEntity<>(createdAirport, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{airportId}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long airportId) {
        if (airportService.getAirportById(airportId).isPresent()) {
            airportService.deleteAirport(airportId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else  {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "{airportId}")
    public ResponseEntity<String> updateAirport(
            @PathVariable("airportId") Long airportId,
            @RequestBody Airport airportUpdate
    ) {
        try {
            airportService.updateAirport(airportId, airportUpdate);
            return new ResponseEntity<>("Airport updated successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
