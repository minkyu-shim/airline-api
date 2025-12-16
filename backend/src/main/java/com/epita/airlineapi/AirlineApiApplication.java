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
import java.time.LocalDateTime;

@SpringBootApplication
public class AirlineApiApplication {

    // Main method to run REST API airline application
    public static void main(String[] args) {
        SpringApplication.run(AirlineApiApplication.class, args);
    }

    // Initialize / Seed Database with records
    @Bean
    @Transactional
    CommandLineRunner initDatabase(PlaneRepository planeRepo,
                                   AirportRepository airportRepo,
                                   FlightRepository flightRepo,
                                   ClientRepository clientRepo) {
        return args -> {
            // Only seed if empty
            // Stop if data already exists to avoid duplicates on "update" mode
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
            Plane airbusA380 = new Plane(null, "Airbus", "A380", 2021); // Big plane

            planeRepo.saveAll(List.of(boeing737, boeing777, airbusA320, airbusA380));

            // ==========================================
            // 3. FLIGHTS
            // ==========================================

            // Helper to make dates easier to read
            LocalDateTime today = LocalDateTime.now();

            List<Flight> flights = Arrays.asList(
                    // Paris -> London (Short Haul / Frequent)
                    createFlight("TO-101", cdg, lhr, airbusA320,
                            today.plusDays(2).withHour(10).withMinute(0), 1.5, 150, 300),
                    createFlight("TO-102", cdg, lhr, boeing737,
                            today.plusDays(2).withHour(16).withMinute(30), 1.5, 140, 280),

                    // London -> New York (Transatlantic)
                    createFlight("TO-201", lhr, jfk, boeing777,
                            today.plusDays(3).withHour(11).withMinute(0), 8.0, 600, 1800),
                    createFlight("TO-202", lhr, jfk, airbusA380,
                            today.plusDays(5).withHour(14).withMinute(0), 8.0, 650, 2100),

                    // New York -> Tokyo (Long Haul)
                    createFlight("TO-301", jfk, hnd, boeing777,
                            today.plusDays(10).withHour(9).withMinute(0), 14.0, 1100, 3500),

                    // Dubai -> Singapore
                    createFlight("TO-401", dxb, sin, airbusA380,
                            today.plusDays(7).withHour(2).withMinute(0), 7.5, 500, 1200),

                    // Singapore -> Paris (Return trip long haul)
                    createFlight("TO-501", sin, cdg, boeing777,
                            today.plusDays(12).withHour(23).withMinute(0), 13.0, 900, 2500),

                    // Domestic / Short hops
                    createFlight("TO-601", lhr, cdg, airbusA320,
                            today.plusDays(4).withHour(8).withMinute(0), 1.5, 120, 250)
            );

            flightRepo.saveAll(flights);

            System.out.println("✅ Database Seeded Successfully: " + flights.size() + " flights added.");
        };
    }

    // Helper method to keep the code above clean
    private Flight createFlight(String number, Airport dep, Airport arr, Plane plane,
                                LocalDateTime date, double durationHours, double ecoPrice, double bizPrice) {
        Flight f = new Flight();
        f.setFlightNumber(number);
        f.setDepartureCity(dep.getAirportCity());
        f.setArrivalCity(arr.getAirportCity());
        f.setDepartureAirport(dep);
        f.setArrivalAirport(arr);
        f.setPlane(plane);
        f.setDepartureDate(date);
        f.setArrivalDate(date.plusMinutes((long)(durationHours * 60)));
        f.setEconomyPrice(BigDecimal.valueOf(ecoPrice));
        f.setBusinessPrice(BigDecimal.valueOf(bizPrice));

        // Rough estimation of seats based on plane type for variety
        if (plane.getPlaneModel().equals("A380")) f.setNumberOfSeats(500);
        else if (plane.getPlaneModel().equals("777")) f.setNumberOfSeats(300);
        else f.setNumberOfSeats(180); // A320, 737

        return f;
    }
}
