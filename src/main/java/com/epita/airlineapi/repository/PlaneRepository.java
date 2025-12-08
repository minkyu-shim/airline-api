package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.Plane;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaneRepository extends JpaRepository<Plane, Long> {
}
