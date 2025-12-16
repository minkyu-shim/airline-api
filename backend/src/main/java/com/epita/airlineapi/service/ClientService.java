package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

    // GET ONE (By Passport)
    public Client getClientByPassport(String passportNumber) {
        return clientRepository.findByPassportNumber(passportNumber)
                .orElseThrow(() -> new NoSuchElementException(
                        "Client with passport no. " + passportNumber + " does not exist"
                ));
    }

    // CREATE
    public Client createClient(Client client) {
        // 1. Check Passport Uniqueness
        if (clientRepository.existsByPassportNumber(client.getPassportNumber())) {
            throw new IllegalStateException("Passport no. " + client.getPassportNumber() + " already exists");
        }

        // 2. Check Email Uniqueness (Inherited from User)
        if (client.getEmail() != null && clientRepository.existsByEmail(client.getEmail())) {
            throw new IllegalStateException("Email " + client.getEmail() + " is already in use");
        }

        return clientRepository.save(client);
    }

    // UPDATE
    @Transactional
    public Client updateClient(String oldPassportNumber, Client newDetails) {
        // 1.Retrieve existing client
        Client existingClient = getClientByPassport(oldPassportNumber);

        // 2.Update Passport Number
        String newPassport = newDetails.getPassportNumber();
        if (newPassport != null && !newPassport.equals(oldPassportNumber)) {
            // Check if the NEW passport is already taken by someone else
            if (clientRepository.existsByPassportNumber(newPassport)) {
                throw new IllegalStateException("Passport no. " + newPassport + " already taken");
            }
            existingClient.setPassportNumber(newPassport);
        }

        // 3.Update User Fields (Inherited)
        if (newDetails.getEmail() != null && !newDetails.getEmail().equals(existingClient.getEmail())) {
            if (clientRepository.existsByEmail(newDetails.getEmail())) {
                throw new IllegalStateException("Email " + newDetails.getEmail() + " already in use");
            }
            existingClient.setEmail(newDetails.getEmail());
        }
        if (newDetails.getFirstName() != null) existingClient.setFirstName(newDetails.getFirstName());
        if (newDetails.getLastName() != null) existingClient.setLastName(newDetails.getLastName());
        if (newDetails.getAddress() != null) existingClient.setAddress(newDetails.getAddress());
        if (newDetails.getPhoneNumber() != null) existingClient.setPhoneNumber(newDetails.getPhoneNumber());
        if (newDetails.getBirthDate() != null) existingClient.setBirthDate(newDetails.getBirthDate());

        // 4.Return updated entity
        return existingClient;
    }

    // DELETE
    @Transactional // Required for deleteBy... derived queries
    public void deleteClient(String passportNumber) {
        if (!clientRepository.existsByPassportNumber(passportNumber)) {
            throw new NoSuchElementException("Client with passport no. " + passportNumber + " does not exist");
        }
        clientRepository.deleteByPassportNumber(passportNumber);
    }
}