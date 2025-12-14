package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Plane;
import com.epita.airlineapi.service.PlaneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/planes") // Added leading slash for best practice
public class PlaneController {

    private final PlaneService planeService;

    public PlaneController(PlaneService planeService) {
        this.planeService = planeService;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Plane>> getPlanes() {
        List<Plane> planes = planeService.getPlanes();

        // Returning 204 No Content if empty is a valid design choice
        if (planes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(planes);
    }

    // GET ONE
    @GetMapping("/{planeId}")
    public ResponseEntity<Plane> getPlaneById(@PathVariable Long planeId) {
        // No try-catch needed.
        // If planeId doesn't exist, Service throws NoSuchElementException -> GlobalHandler catches it.
        Plane plane = planeService.getPlaneById(planeId);
        return ResponseEntity.ok(plane);
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Plane> createPlane(@RequestBody Plane plane) {
        Plane createdPlane = planeService.savePlane(plane);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlane);
    }

    // DELETE
    @DeleteMapping("/{planeId}")
    public ResponseEntity<Void> deletePlane(@PathVariable Long planeId) {
        // No try-catch needed.
        // If planeId doesn't exist, Service throws exception -> GlobalHandler catches it.
        planeService.deletePlane(planeId);
        return ResponseEntity.noContent().build();
    }

    // UPDATE
    @PutMapping("/{planeId}")
    public ResponseEntity<Plane> updatePlane(@PathVariable Long planeId, @RequestBody Plane planeUpdate) {
        // No try-catch needed.
        // Catches NoSuchElementException (404) AND IllegalArgumentException (400) automatically.
        Plane updatedPlane = planeService.updatePlane(planeId, planeUpdate);
        return ResponseEntity.ok(updatedPlane);
    }
}