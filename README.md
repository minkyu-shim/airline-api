# Timeout Airline

A backend web application that manages booking for airline company Timeout Airline, which allows customers to make online bookings for a flight.

*This project is currently under development.*

## Features

1.  **User Management:** REST API to add, display, update, and delete Users.
2.  **Plane Management:** REST API to add, display, update, and delete Planes.
3.  **Airport Management:** REST API to add, display, update, and delete Airports.
4.  **Flight Management:** REST API to add, display, update, and delete Flights.
5.  **Client/Employee Management:** REST API to create, read, update, and delete Employees/Clients.
6.  **Flight Booking:** A REST API for customers to book a flight.
7.  **Miles Reward Program:** Record each booking and generate a random discount code for a user after 3 flights within the same civil year. (Partially implemented)

## Data Model

-   **User**: `Id_User`, `Firstname`, `Lastname`, `Address`, `Email`, `Phone`, `Birthdate`
-   **Client**: `NumPassport`
-   **Employee**: `NumEmp`, `Profession`, `title`
-   **Plane**: `IdPlane`, `Brand`, `Model`, `ManufacturingYear`
-   **Airport**: `Id_airport`, `NameAirport`, `CountryAirport`, `CityAirport`
-   **Flight**: `FlightNumber`, `DepartureCity`, `ArrivalCity`, `DepartureHour`, `ArrivalHour`, `DepartureAirport`, `ArrivalAirport`, `IdPlane`, `NumberOfSeat`, `firstClassSeatPrice`, `PremiumSeatPrice`, `BusinessClassPrice`, `EcomicsClassPrice`
-   **Book**: `IdReservation`, `IdFlight`, `IdClient`, `TypeOfSeat`
-   **MilesReward**: `id_client`, `id_flight`, `date`

## Technologies Used

-   Java 25
-   Spring Boot 4.0.0
-   Spring Web MVC
-   Spring Data JPA
-   PostgreSQL
-   Maven
-   Lombok
-   Docker

## Getting Started

### Prerequisites

-   Java 25 or later
-   Maven
-   Docker (optional, for running the database)

### Installation

1.  Clone the repository:
    ```bash
    git clone <repository-url>
    ```
2.  Navigate to the project directory:
    ```bash
    cd airline-api
    ```
3.  Run the application using Maven:
    ```bash
    ./mvnw spring-boot:run
    ```

### Docker

The project includes a `docker-compose.yml` file to run a PostgreSQL database in a Docker container.

1.  Make sure you have Docker installed and running.
2.  Start the database container:
    ```bash
    docker-compose up -d
    ```

The API will then be able to connect to the database on `localhost:5332`.

## API Endpoints

The following are the available API endpoints:

### Airport

-   `GET /api/v1/airport`: Get all airports
-   `GET /api/v1/airport/{airportId}`: Get an airport by ID
-   `POST /api/v1/airport`: Create a new airport
-   `PUT /api/v1/airport/{airportId}`: Update an airport
-   `DELETE /api/v1/airport/{airportId}`: Delete an airport

### Book

-   `GET /api/v1/books`: Get all books
-   `GET /api/v1/books/{id}`: Get a book by ID
-   `POST /api/v1/books`: Create a new book
-   `PUT /api/v1/books/{id}`: Update a book
-   `DELETE /api/v1/books/{id}`: Delete a book

### Client

-   `GET /api/v1/clients`: Get all clients
-   `GET /api/v1/clients/{passportNumber}`: Get a client by passport number
-   `POST /api/v1/clients`: Create a new client
-   `PUT /api/v1/clients/{passportNumber}`: Update a client
-   `DELETE /api/v1/clients/{passportNumber}`: Delete a client

### Flight

-   `GET /api/v1/flight`: Get all flights
-   `GET /api/v1/flight/{flightId}`: Get a flight by ID
-   `POST /api/v1/flight`: Create a new flight
-   `PUT /api/v1/flight/{flightId}`: Update a flight
-   `DELETE /api/v1/flight/{flightId}`: Delete a flight

### Miles Reward

-   `GET /api/miles-rewards`: Get all rewards
-   `GET /api/miles-rewards/{id}`: Get a reward by ID
-   `POST /api/miles-rewards`: Create a new reward
-   `PUT /api/miles-rewards/{id}`: Update a reward
-   `DELETE /api/miles-rewards/{id}`: Delete a reward

### Plane

-   `GET /api/v1/plane`: Get all planes
-   `GET /api/v1/plane/{planeId}`: Get a plane by ID
-   `POST /api/v1/plane`: Create a new plane
-   `PUT /api/v1/plane/{planeId}`: Update a plane
-   `DELETE /api/v1/plane/{planeId}`: Delete a plane

### User

-   `GET /api/v1/user`: Get all users
-   `GET /api/v1/user/{userId}`: Get a user by ID
-   `POST /api/v1/user`: Create a new user
-   `PUT /api/v1/user/{userId}`: Update a user
-   `DELETE /api/v1/user/{userId}`: Delete a user

## Future Work

-   **Implement Flight Search:** Create the endpoint for searching available flights.
-   **Add Frontend UI:** Connect a basic frontend to the backend API.
-   **Enhance Seat Management:** Ensure a user cannot book a flight with no available seats.
-   **Testing:** Prepare comprehensive test scenarios and data for the project presentation.
