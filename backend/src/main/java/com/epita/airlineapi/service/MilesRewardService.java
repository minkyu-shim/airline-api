package com.epita.airlineapi.service;

import com.epita.airlineapi.dto.MilesRewardCreateDto;
import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.model.Flight;
import com.epita.airlineapi.model.MilesReward;
import com.epita.airlineapi.repository.BookRepository;
import com.epita.airlineapi.repository.ClientRepository;
import com.epita.airlineapi.repository.FlightRepository;
import com.epita.airlineapi.repository.MilesRewardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Automatically injects final repositories
public class MilesRewardService {

    private final MilesRewardRepository milesRewardRepository;
    private final ClientRepository clientRepository; // Needed to resolve Client ID
    private final FlightRepository flightRepository; // Needed to resolve Flight ID
    private final BookRepository bookRepository; // need for miles reward thing

    @Transactional
    public MilesReward createReward(MilesRewardCreateDto dto) {
        if (dto == null || dto.getClientId() == null || dto.getFlightId() == null || dto.getDate() == null) {
            throw new IllegalArgumentException("clientId, flightId and date are required");
        }

        // 1. Fetch the Client
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + dto.getClientId()));

        // 2. Fetch the Flight
        Flight flight = flightRepository.findById(dto.getFlightId())
                .orElseThrow(() -> new EntityNotFoundException("Flight not found with ID: " + dto.getFlightId()));

        // 3. Create the Reward
        MilesReward reward = new MilesReward();
        reward.setClient(client);
        reward.setFlight(flight);
        reward.setDate(dto.getDate());

        // 4. Save the reward first
        MilesReward savedReward = milesRewardRepository.save(reward);

        // === 5. DISCOUNT LOGIC START ===

        // Get current year
        int currentYear = LocalDate.now().getYear();

        // Count how many bookings a specific client has in the current year
        long flightCount = bookRepository.countFlightsByClientAndYear(client, currentYear);

        // Check if it is a multiple of 3
        if (flightCount > 0 && flightCount % 3 == 0) {
            // Generate a short random code (e.g., "DISC-A1B2")
            String code = "DISC-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

            // Update Client
            client.setDiscountCode(code);
            clientRepository.save(client);
        }
        // === DISCOUNT LOGIC END ===

        return savedReward;
    }

    public List<MilesReward> getAllRewards() {
        return milesRewardRepository.findAll();
    }

    public MilesReward getRewardById(Long id) {
        return milesRewardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reward not found with ID: " + id));
    }

    @Transactional
    public MilesReward updateReward(Long id, MilesRewardCreateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Payload is required");
        }

        // 1.Find the existing reward
        MilesReward existingReward = milesRewardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reward not found with ID: " + id));

        // 2.Update Client (only if ID changed)
        if (dto.getClientId() != null && !dto.getClientId().equals(existingReward.getClient().getUserId())) { // Assuming Client has getId()
            Client newClient = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + dto.getClientId()));
            existingReward.setClient(newClient);
        }

        // 3.Update Flight (only if ID changed)
        if (dto.getFlightId() != null && !dto.getFlightId().equals(existingReward.getFlight().getFlightId())) { // Assuming Flight has getId()
            Flight newFlight = flightRepository.findById(dto.getFlightId())
                    .orElseThrow(() -> new EntityNotFoundException("Flight not found with ID: " + dto.getFlightId()));
            existingReward.setFlight(newFlight);
        }

        // 4.Update Date
        if (dto.getDate() != null) {
            existingReward.setDate(dto.getDate());
        }

        // 5.Save (The @Transactional annotation will actually trigger the update automatically, but .save is good practice)
        return milesRewardRepository.save(existingReward);
    }

    public void deleteReward(Long id) {
        if (!milesRewardRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. Reward not found with ID: " + id);
        }
        milesRewardRepository.deleteById(id);
    }
}
