package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Flight;
import com.epita.airlineapi.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/flight")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<?> getFlights() {
        List<Flight> flights = flightService.getFlights();

        if (flights.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping(path = "{flightId}")
    public ResponseEntity<?> getFlightById(@PathVariable Long flightId) {
        Optional<Flight> flight = flightService.getFlightById(flightId);

        return flight.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        Flight createdFlight = flightService.saveFlight(flight);
        return new ResponseEntity<>(createdFlight, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{flightId}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long flightId) {
        if (flightService.getFlightById(flightId).isPresent()) {
            flightService.deleteFlight(flightId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "{flightId}")
    public ResponseEntity<?> updateFlight(
            @PathVariable Long flightId,
            @RequestBody Flight flightUpdate
    ) {
        try {
            flightService.updateFlight(flightId, flightUpdate);
            return new ResponseEntity<>("Flight updated successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
