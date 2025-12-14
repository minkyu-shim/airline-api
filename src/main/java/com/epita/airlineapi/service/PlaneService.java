package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Plane;
import com.epita.airlineapi.repository.PlaneRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException; // Standard Java exception for "Not Found"
import java.util.Objects;

@Service
public class PlaneService {

    private final PlaneRepository planeRepository;

    public PlaneService(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;
    }

    // GET ALL
    public List<Plane> getPlanes() {
        return planeRepository.findAll();
    }

    // GET ONE (Refactored to throw exception if missing, matching ClientService)
    public Plane getPlaneById(Long planeId) {
        return planeRepository.findById(planeId)
                .orElseThrow(() -> new NoSuchElementException("Plane with id " + planeId + " does not exist"));
    }

    // CREATE
    public Plane savePlane(Plane plane) {
        // Optional: Add logic here (e.g. check if Manufacturing Year is valid?)
        if (plane.getManufacturingYear() != null && plane.getManufacturingYear() < 1900) {
            throw new IllegalArgumentException("Invalid manufacturing year");
        }
        return planeRepository.save(plane);
    }

    // DELETE
    public void deletePlane(Long planeId) {
        if (!planeRepository.existsById(planeId)) {
            throw new NoSuchElementException("Plane with id " + planeId + " does not exist");
        }
        planeRepository.deleteById(planeId);
    }

    // UPDATE
    @Transactional
    public Plane updatePlane(Long planeId, Plane updateRequest) {
        // Retrieve existing (Re-use the method above)
        Plane plane = getPlaneById(planeId);

        // Update Plane Brand
        if (updateRequest.getPlaneBrand() != null &&
                !updateRequest.getPlaneBrand().isEmpty() &&
                !Objects.equals(plane.getPlaneBrand(), updateRequest.getPlaneBrand())) {
            plane.setPlaneBrand(updateRequest.getPlaneBrand());
        }

        // Update Plane Model
        if (updateRequest.getPlaneModel() != null &&
                !updateRequest.getPlaneModel().isEmpty() &&
                !Objects.equals(plane.getPlaneModel(), updateRequest.getPlaneModel())) {
            plane.setPlaneModel(updateRequest.getPlaneModel());
        }

        // Update Manufacturing Year
        if (updateRequest.getManufacturingYear() != null &&
                !Objects.equals(plane.getManufacturingYear(), updateRequest.getManufacturingYear())) {
            plane.setManufacturingYear(updateRequest.getManufacturingYear());
        }

        // Return the updated object (Changes are auto-saved by @Transactional)
        return plane;
    }
}