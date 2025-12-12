package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // GET ALL
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    // GET ONE
    public Client getClientById(String passportNumber) {
        // Exception Handled by GlobalExceptionHandler
        return clientRepository.findById(passportNumber)
                .orElseThrow(() -> new NoSuchElementException(
                        "Client with passport no. " + passportNumber + " does not exist"
                ));
    }

    // CREATE
    public Client createClient(Client client) {
        Optional<Client> optionalClient = clientRepository.findById(client.getPassportNumber());
        if (optionalClient.isPresent()) {
            throw new IllegalStateException("Client with passport no. " +
                    client.getPassportNumber() + " already exists");
        }
        return clientRepository.save(client);
    }

    // UPDATE
    @Transactional  // Required for atomicity and to detect changes and auto-run UPDATE sql cmd
    public Client updateClient(String oldPassportNumber, Client newDetails) {
        String newPassportNumber = newDetails.getPassportNumber();

        // If no change, return immediately
        if (newPassportNumber == null || newPassportNumber.equals(oldPassportNumber)) {
            return new Client(oldPassportNumber);
        }

        // Is the new ID already taken?
        // We use standard existsById() passing the new number
        if (clientRepository.existsById(newPassportNumber)) {
            throw new IllegalStateException("Passport no." + newPassportNumber + " already taken");
        }

        // Does the OLD record exist?
        if (!clientRepository.existsById(oldPassportNumber)) {
            throw new NoSuchElementException("Client " + oldPassportNumber + " not found");
        }

        // Force the ID update by query stated in repo
        clientRepository.updatePassportNumber(oldPassportNumber, newPassportNumber);

        // Return the object manually since we know the only field
        Client updatedClient = new Client();
        updatedClient.setPassportNumber(newPassportNumber);
        return updatedClient;
    }

    // DELETE
    public void deleteClient(String passportNumber) {
        boolean exists = clientRepository.existsById(passportNumber);

        // 404 NOT FOUND
        if (!exists) {
            throw new NoSuchElementException("Client with passport no. " + passportNumber + " does not exist");
        }

        clientRepository.deleteById(passportNumber);
    }
}
