package com.epita.airlineapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airportId;

    private String airportName;

    private String airportCountry;

    private String airportCity;

    public Airport() {
    }

    public Airport(Long airportId, String airportName, String airportCountry, String airportCity) {
        this.airportId = airportId;
        this.airportName = airportName;
        this.airportCountry = airportCountry;
        this.airportCity = airportCity;
    }

    public Long getAirportId() {
        return airportId;
    }

    public void setAirportId(Long airportId) {
        this.airportId = airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getAirportCountry() {
        return airportCountry;
    }

    public void setAirportCountry(String airportCountry) {
        this.airportCountry = airportCountry;
    }

    public String getAirportCity() {
        return airportCity;
    }

    public void setAirportCity(String airportCity) {
        this.airportCity = airportCity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(airportId, airport.airportId) && Objects.equals(airportName, airport.airportName) && Objects.equals(airportCountry, airport.airportCountry) && Objects.equals(airportCity, airport.airportCity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airportId, airportName, airportCountry, airportCity);
    }
}
