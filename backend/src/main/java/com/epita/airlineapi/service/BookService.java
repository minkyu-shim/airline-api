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

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;
    private final FlightRepository flightRepository;

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
    public Book createBook(Book book) {
        // 1.Validate Client Relationship
        if (book.getClient() == null || book.getClient().getUserId() == null) {
            throw new IllegalArgumentException("Booking must have a valid Client ID");
        }
        // Fetch the full Client entity from DB to ensure it exists
        Client client = clientRepository.findById(book.getClient().getUserId())
                .orElseThrow(() -> new NoSuchElementException("Client with ID " + book.getClient().getUserId() + " not found"));
        book.setClient(client);

        // 2.Validate Flight Relationship
        if (book.getFlight() == null || book.getFlight().getFlightId() == null) {
            throw new IllegalArgumentException("Booking must have a valid Flight ID");
        }
        // Fetch the full Flight entity from DB
        Flight flight = flightRepository.findById(book.getFlight().getFlightId())
                .orElseThrow(() -> new NoSuchElementException("Flight with ID " + book.getFlight().getFlightId() + " not found"));
        book.setFlight(flight);

        // 3.Save
        return bookRepository.save(book);
    }

    // UPDATE
    @Transactional
    public Book updateBook(Long id, Book updateRequest) {
        Book existingBook = getBookById(id);

        // 1.Update Seat Type
        if (updateRequest.getTypeOfSeat() != null &&
                !updateRequest.getTypeOfSeat().isEmpty() &&
                !Objects.equals(existingBook.getTypeOfSeat(), updateRequest.getTypeOfSeat())) {
            existingBook.setTypeOfSeat(updateRequest.getTypeOfSeat());
        }

        // 2.Change Flight (if needed)
        if (updateRequest.getFlight() != null && updateRequest.getFlight().getFlightId() != null) {
            Long newFlightId = updateRequest.getFlight().getFlightId();
            if (!newFlightId.equals(existingBook.getFlight().getFlightId())) {
                Flight newFlight = flightRepository.findById(newFlightId)
                        .orElseThrow(() -> new NoSuchElementException("New Flight ID " + newFlightId + " not found"));
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
}