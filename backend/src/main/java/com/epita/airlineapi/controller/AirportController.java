package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Airport;
import com.epita.airlineapi.service.AirportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/airports")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Airport>> getAirports() {
        List<Airport> airports = airportService.getAirports();
        if (airports.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(airports);
    }

    // GET ONE
    @GetMapping(path = "/{airportId}")
    public ResponseEntity<Airport> getAirportById(@PathVariable Long airportId) {
        // Service throws NoSuchElementException if missing -> Caught by GlobalHandler (404)
        return ResponseEntity.ok(airportService.getAirportById(airportId));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Airport> createAirport(@RequestBody Airport airport) {
        Airport createdAirport = airportService.saveAirport(airport);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAirport);
    }

    // UPDATE
    @PutMapping(path = "/{airportId}")
    public ResponseEntity<Airport> updateAirport(
            @PathVariable Long airportId,
            @RequestBody Airport airportUpdate) {

        // Returns the updated JSON object
        Airport updated = airportService.updateAirport(airportId, airportUpdate);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping(path = "/{airportId}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long airportId) {
        airportService.deleteAirport(airportId);
        return ResponseEntity.noContent().build();
    }
}