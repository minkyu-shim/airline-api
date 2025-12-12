package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // GET: Get clients
    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        List<Client> clients = clientService.getClients();

        if (clients.isEmpty()) {
            // Error 204's body is ignored, so following syntax is used
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    // GET: Get client by id
    @GetMapping("{passportNumber}")
    public ResponseEntity<Client> getClientById(@PathVariable String passportNumber) {
        // If missing, ClientService method .getClientById(...) throws NoSuchElementException,
        // caught by GlobalExceptionHandler; i.e. no need for Optional, .map(). and Optional.orElseGet()
        Client client = clientService.getClientById(passportNumber);

        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    // POST: create client
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        // If data is Bad (@Valid fails), IllegalStateException is caught by GlobalExceptionHandler
        Client createdClient = clientService.createClient(client);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    // UPDATE: update client
    @PutMapping("{passportNumber}")
    public ResponseEntity<Client> updateClient(@PathVariable String passportNumber, @RequestBody Client newDetails) {
        // If IllegalStateException happens, GlobalHandler catches it.
        Client updatedClient = clientService.updateClient(passportNumber, newDetails);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    // DELETE: delete client
    @DeleteMapping("{passportNumber}")
    public ResponseEntity<Void> deleteClient(@PathVariable String passportNumber) {
        // If it doesn't exist, Service throws NoSuchElementException.
        clientService.deleteClient(passportNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
