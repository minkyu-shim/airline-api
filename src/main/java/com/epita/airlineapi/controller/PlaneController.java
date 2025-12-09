package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Plane;
import com.epita.airlineapi.service.PlaneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/plane")
public class PlaneController {
    private final PlaneService planeService;

    public PlaneController(PlaneService planeService){
        this.planeService = planeService;
    }

    @GetMapping
    public ResponseEntity<?> getPlanes() {
        List<Plane> planes = planeService.getPlanes();

        if (planes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(planes, HttpStatus.OK);
    }

    @GetMapping(path = "{planeId}")
    public ResponseEntity<Plane> getPlaneById(@PathVariable Long planeId) {
        Optional<Plane> plane = planeService.getPlaneById(planeId);

        return plane.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Plane> createPlane(@RequestBody Plane plane) {
        Plane createdPlane = planeService.savePlane(plane);
        return new ResponseEntity<>(createdPlane, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{planeId}")
    public ResponseEntity<Void> deletePlane(@PathVariable Long planeId) {
        if (planeService.getPlaneById(planeId).isPresent()) {
            planeService.deletePlane(planeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "{planeId}")
    public ResponseEntity<?> updatePlane(
            @PathVariable Long planeId,
            @RequestBody Plane planeUpdate
    ) {
        try {
            planeService.updatePlane(planeId, planeUpdate);
            return new ResponseEntity<>("Plane updated successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
