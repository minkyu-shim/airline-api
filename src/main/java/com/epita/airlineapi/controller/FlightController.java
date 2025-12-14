package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Flight;
import com.epita.airlineapi.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Flight>> getFlights() {
        List<Flight> flights = flightService.getFlights();
        if (flights.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(flights);
    }

    // GET ONE (By ID)
    @GetMapping(path = "/{flightId}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long flightId) {
        // GlobalExceptionHandler catches NoSuchElementException -> 404
        return ResponseEntity.ok(flightService.getFlightById(flightId));
    }

    // GET ONE (By Flight Number)
    @GetMapping(path = "/number/{flightNumber}")
    public ResponseEntity<Flight> getFlightByNumber(@PathVariable String flightNumber) {
        return ResponseEntity.ok(flightService.getFlightByNumber(flightNumber));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        // GlobalExceptionHandler catches IllegalStateException (Invalid dates/Duplicate ID) -> 400
        Flight createdFlight = flightService.saveFlight(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFlight);
    }

    // UPDATE
    @PutMapping(path = "/{flightId}")
    public ResponseEntity<Flight> updateFlight(
            @PathVariable Long flightId,
            @RequestBody Flight flightUpdate) {

        // Returns updated JSON object
        Flight updatedFlight = flightService.updateFlight(flightId, flightUpdate);
        return ResponseEntity.ok(updatedFlight);
    }

    // DELETE
    @DeleteMapping(path = "/{flightId}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long flightId) {
        flightService.deleteFlight(flightId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Flight> searchFlights(
        @RequestParam String from,
        @RequestParam String to,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
) {
    return flightService.searchFlights(from, to, date);
}
}