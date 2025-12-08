package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Plane;
import com.epita.airlineapi.repository.PlaneRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PlaneService {
    private final PlaneRepository planeRepository;

    public PlaneService(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;
    }

    public List<Plane> getPlanes() {
        return planeRepository.findAll();
    }

    public Optional<Plane> getPlaneById(Long planeId) {
        return planeRepository.findById(planeId);
    }

    public Plane savePlane(Plane plane) {
        return planeRepository.save(plane);
    }

    public void deletePlane(Long planeId) {

        boolean exists = planeRepository.existsById(planeId);

        if (!exists) {
            throw new EntityNotFoundException(
                    "Plane with id " + planeId + " does not exist"
            );
        }

        planeRepository.deleteById(planeId);
    }

    @Transactional
    public void updatePlane(Long planeId, Plane updateRequest) {
        // 1. Retrieve the existing plane
        // We assume planeRepository exists and extends JpaRepository
        Plane plane = planeRepository.findById(planeId)
                .orElseThrow(() -> new IllegalStateException("Plane with id " + planeId + " does not exist"));

        // 2. Update Plane Brand
        if (updateRequest.getPlaneBrand() != null &&
                !updateRequest.getPlaneBrand().isEmpty() &&
                !Objects.equals(plane.getPlaneBrand(), updateRequest.getPlaneBrand())) {
            plane.setPlaneBrand(updateRequest.getPlaneBrand());
        }

        // 3. Update Plane Model
        if (updateRequest.getPlaneModel() != null &&
                !updateRequest.getPlaneModel().isEmpty() &&
                !Objects.equals(plane.getPlaneModel(), updateRequest.getPlaneModel())) {
            plane.setPlaneModel(updateRequest.getPlaneModel());
        }

        // 4. Update Manufacturing Year
        // Note: Integers do not use .isEmpty(), so we only check for null and value difference.
        if (updateRequest.getManufacturingYear() != null &&
                !Objects.equals(plane.getManufacturingYear(), updateRequest.getManufacturingYear())) {
            plane.setManufacturingYear(updateRequest.getManufacturingYear());
        }
    }
}
