package com.epita.airlineapi;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.epita.airlineapi.repository.*;
import com.epita.airlineapi.model.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate; // ✅ Using LocalDate now

@SpringBootApplication
public class AirlineApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirlineApiApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner initDatabase(PlaneRepository planeRepo,
                                   AirportRepository airportRepo,
                                   FlightRepository flightRepo,
                                   ClientRepository clientRepo) {
        return args -> {
            // Stop if data already exists
            if (planeRepo.count() > 0) return;

            System.out.println("🌱 Starting Database Seeding...");

            // ==========================================
            // 1. AIRPORTS
            // ==========================================
            Airport cdg = new Airport(null, "Charles de Gaulle", "France", "Paris");
            Airport lhr = new Airport(null, "Heathrow", "UK", "London");
            Airport jfk = new Airport(null, "JFK International", "USA", "New York");
            Airport hnd = new Airport(null, "Haneda", "Japan", "Tokyo");
            Airport dxb = new Airport(null, "Dubai International", "UAE", "Dubai");
            Airport sin = new Airport(null, "Changi", "Singapore", "Singapore");

            airportRepo.saveAll(List.of(cdg, lhr, jfk, hnd, dxb, sin));

            // ==========================================
            // 2. PLANES
            // ==========================================
            Plane boeing737 = new Plane(null, "Boeing", "737", 2015);
            Plane boeing777 = new Plane(null, "Boeing", "777", 2019);
            Plane airbusA320 = new Plane(null, "Airbus", "A320", 2018);
            Plane airbusA380 = new Plane(null, "Airbus", "A380", 2021);

            planeRepo.saveAll(List.of(boeing737, boeing777, airbusA320, airbusA380));

            // ==========================================
            // 3. FLIGHTS
            // ==========================================

            // ✅ 1. Use a fixed LocalDate (Christmas 2025)
            LocalDate baseDate = LocalDate.of(2025, 12, 25);

            List<Flight> flights = Arrays.asList(
                    // Paris -> London (Same Day)
                    createFlight("TO-101", cdg, lhr, airbusA320, baseDate, 1.5, 150, 300),
                    createFlight("TO-102", cdg, lhr, boeing737, baseDate, 1.5, 140, 280),

                    // London -> New York (Starts Dec 26)
                    createFlight("TO-201", lhr, jfk, boeing777, baseDate.plusDays(1), 8.0, 600, 1800),

                    // London -> New York (Starts Dec 27)
                    createFlight("TO-202", lhr, jfk, airbusA380, baseDate.plusDays(2), 8.0, 650, 2100),

                    // New York -> Tokyo (Starts Dec 28)
                    createFlight("TO-301", jfk, hnd, boeing777, baseDate.plusDays(3), 14.0, 1100, 3500),

                    // Dubai -> Singapore (Dec 25)
                    createFlight("TO-401", dxb, sin, airbusA380, baseDate, 7.5, 500, 1200),

                    // Singapore -> Paris (Dec 29)
                    createFlight("TO-501", sin, cdg, boeing777, baseDate.plusDays(4), 13.0, 900, 2500)
            );

            flightRepo.saveAll(flights);

            System.out.println("✅ Database Seeded Successfully");
        };
    }

    // ✅ Helper method updated to take LocalDate
    private Flight createFlight(String number, Airport dep, Airport arr, Plane plane,
                                LocalDate date, double durationHours, double ecoPrice, double bizPrice) {
        Flight f = new Flight();
        f.setFlightNumber(number);
        f.setDepartureCity(dep.getAirportCity());
        f.setArrivalCity(arr.getAirportCity());
        f.setDepartureAirport(dep);
        f.setArrivalAirport(arr);
        f.setPlane(plane);

        // ✅ Set Date directly (No time component)
        f.setDepartureDate(date);

        // Logic: If duration > 12 hours, assume arrival is next day. Else same day.
        if (durationHours > 12) {
            f.setArrivalDate(date.plusDays(1));
        } else {
            f.setArrivalDate(date);
        }

        f.setEconomyPrice(BigDecimal.valueOf(ecoPrice));
        f.setBusinessPrice(BigDecimal.valueOf(bizPrice));

        if (plane.getPlaneModel().equals("A380")) f.setNumberOfSeats(500);
        else if (plane.getPlaneModel().equals("777")) f.setNumberOfSeats(300);
        else f.setNumberOfSeats(180);

        return f;
    }
}