package com.epita.airlineapi.service;

import com.epita.airlineapi.dto.MilesRewardCreateDto;
import com.epita.airlineapi.model.Client;
import com.epita.airlineapi.model.Flight;
import com.epita.airlineapi.model.MilesReward;
import com.epita.airlineapi.repository.ClientRepository;
import com.epita.airlineapi.repository.FlightRepository;
import com.epita.airlineapi.repository.MilesRewardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // Automatically injects final repositories
public class MilesRewardService {

    private final MilesRewardRepository milesRewardRepository;
    private final ClientRepository clientRepository; // Needed to resolve Client ID
    private final FlightRepository flightRepository; // Needed to resolve Flight ID

    @Transactional
    public MilesReward createReward(MilesRewardCreateDto dto) {
        // 1.Fetch the Client
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + dto.getClientId()));

        // 2.Fetch the Flight
        Flight flight = flightRepository.findById(dto.getFlightId())
                .orElseThrow(() -> new EntityNotFoundException("Flight not found with ID: " + dto.getFlightId()));

        // 3.Create the Reward
        MilesReward reward = new MilesReward();
        reward.setClient(client);
        reward.setFlight(flight);
        reward.setDate(dto.getDate());

        // 4.Save and return
        return milesRewardRepository.save(reward);
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