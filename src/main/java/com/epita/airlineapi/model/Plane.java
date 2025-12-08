package com.epita.airlineapi.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planeId;

    private String planeBrand;

    private String planeModel;

    private Integer manufacturingYear;

    public Plane() {
    }

    public Plane(Long planeId, String planeBrand, String planeModel, Integer manufacturingYear) {
        this.planeId = planeId;
        this.planeBrand = planeBrand;
        this.planeModel = planeModel;
        this.manufacturingYear = manufacturingYear;
    }

    public Long getPlaneId() {
        return planeId;
    }

    public void setPlaneId(Long planeId) {
        this.planeId = planeId;
    }

    public String getPlaneBrand() {
        return planeBrand;
    }

    public void setPlaneBrand(String planeBrand) {
        this.planeBrand = planeBrand;
    }

    public String getPlaneModel() {
        return planeModel;
    }

    public void setPlaneModel(String planeModel) {
        this.planeModel = planeModel;
    }

    public Integer getManufacturingYear() {
        return manufacturingYear;
    }

    public void setManufacturingYear(Integer manufacturingYear) {
        this.manufacturingYear = manufacturingYear;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Plane plane = (Plane) o;
        return Objects.equals(planeId, plane.planeId) && Objects.equals(planeBrand, plane.planeBrand) && Objects.equals(planeModel, plane.planeModel) && Objects.equals(manufacturingYear, plane.manufacturingYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planeId, planeBrand, planeModel, manufacturingYear);
    }
}
