package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        List<Client> clients = clientService.getClients();
        if (clients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clients);
    }

    // GET ONE (Using Passport Number)
    @GetMapping("/{passportNumber}")
    public ResponseEntity<Client> getClient(@PathVariable String passportNumber) {
        // GlobalExceptionHandler catches NoSuchElementException -> 404
        return ResponseEntity.ok(clientService.getClientByPassport(passportNumber));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        // GlobalExceptionHandler catches IllegalStateException (Passport/Email taken) -> 400
        Client createdClient = clientService.createClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    // UPDATE
    @PutMapping("/{passportNumber}")
    public ResponseEntity<Client> updateClient(
            @PathVariable String passportNumber,
            @RequestBody Client client) {

        // Returns the full updated JSON object
        Client updatedClient = clientService.updateClient(passportNumber, client);
        return ResponseEntity.ok(updatedClient);
    }

    // DELETE
    @DeleteMapping("/{passportNumber}")
    public ResponseEntity<Void> deleteClient(@PathVariable String passportNumber) {
        clientService.deleteClient(passportNumber);
        return ResponseEntity.noContent().build();
    }
}