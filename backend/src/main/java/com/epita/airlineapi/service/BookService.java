package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Book;
import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.model.Flight;
import com.epita.airlineapi.repository.BookRepository;
import com.epita.airlineapi.repository.ClientRepository;
import com.epita.airlineapi.repository.FlightRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;
    private final FlightRepository flightRepository;
    private static final Set<String> ALLOWED_SEAT_TYPES = Set.of("ECONOMY", "BUSINESS");

    public BookService(BookRepository bookRepository,
                       ClientRepository clientRepository,
                       FlightRepository flightRepository) {
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.flightRepository = flightRepository;
    }

    // GET ALL
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    // GET ONE
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation " + id + " not found"));
    }

    // CREATE
    @Transactional
    public Book createBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Booking payload is required");
        }

        // Seat type validation
        validateSeatType(book.getTypeOfSeat());

        // Validate Client Relationship
        if (book.getClient() == null || book.getClient().getUserId() == null) {
            throw new IllegalArgumentException("Booking must have a valid Client ID");
        }

        // Fetch the full Client entity from DB to ensure it exists
        Client client = clientRepository.findById(book.getClient().getUserId())
                .orElseThrow(() -> new NoSuchElementException("Client with ID " + book.getClient().getUserId() + " not found"));
        book.setClient(client);

        // Validate Flight Relationship
        if (book.getFlight() == null || book.getFlight().getFlightId() == null) {
            throw new IllegalArgumentException("Booking must have a valid Flight ID");
        }

        // Fetch the full Flight entity from DB
        Flight flight = flightRepository.findById(book.getFlight().getFlightId())
                .orElseThrow(() -> new NoSuchElementException("Flight with ID " + book.getFlight().getFlightId() + " not found"));

        validateSeatAvailability(flight, client);
        book.setFlight(flight);

        return bookRepository.save(book);
    }

    // UPDATE
    @Transactional
    public Book updateBook(Long id, Book updateRequest) {
        Book existingBook = getBookById(id);

        // 1.Update Seat Type
        if (updateRequest != null &&
                updateRequest.getTypeOfSeat() != null &&
                !updateRequest.getTypeOfSeat().isBlank() &&
                !Objects.equals(existingBook.getTypeOfSeat(), updateRequest.getTypeOfSeat())) {

            validateSeatType(updateRequest.getTypeOfSeat());
            existingBook.setTypeOfSeat(updateRequest.getTypeOfSeat());
        }

        // 2.Change Flight (if needed)
        if (updateRequest != null && updateRequest.getFlight() != null && updateRequest.getFlight().getFlightId() != null) {
            Long newFlightId = updateRequest.getFlight().getFlightId();
            Long currentFlightId = existingBook.getFlight() != null ? existingBook.getFlight().getFlightId() : null;

            if (!newFlightId.equals(currentFlightId)) {
                Flight newFlight = flightRepository.findById(newFlightId)
                        .orElseThrow(() -> new NoSuchElementException("New Flight ID " + newFlightId + " not found"));

                validateSeatAvailability(newFlight, existingBook.getClient());
                existingBook.setFlight(newFlight);
            }
        }

        // We typically don't change the client of a booking.
        return existingBook; // @Transactional auto-saves changes
    }

    // DELETE
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NoSuchElementException("Reservation " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

    private void validateSeatType(String seatType) {
        if (seatType == null || seatType.isBlank()) {
            throw new IllegalArgumentException("typeOfSeat is required");
        }

        if (!ALLOWED_SEAT_TYPES.contains(seatType.toUpperCase())) {
            throw new IllegalArgumentException("typeOfSeat must be one of " + ALLOWED_SEAT_TYPES);
        }
    }

    private void validateSeatAvailability(Flight flight, Client client) {
        if (flight.getNumberOfSeats() == null || flight.getNumberOfSeats() <= 0) {
            throw new IllegalStateException("Flight seat capacity is not configured");
        }

        if (bookRepository.existsByFlight_FlightIdAndClient_UserId(flight.getFlightId(), client.getUserId())) {
            throw new IllegalStateException("Client already has a reservation on this flight");
        }

        long currentReservations = bookRepository.countByFlight_FlightId(flight.getFlightId());
        if (currentReservations >= flight.getNumberOfSeats()) {
            throw new IllegalStateException("No seats available on this flight");
        }
    }
}

