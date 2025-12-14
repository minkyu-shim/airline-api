package com.epita.airlineapi.controller;

import com.epita.airlineapi.dto.MilesRewardCreateDto;
import com.epita.airlineapi.model.MilesReward;
import com.epita.airlineapi.service.MilesRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/miles-rewards")
@RequiredArgsConstructor
public class MilesRewardController {

    private final MilesRewardService milesRewardService;

    @PostMapping
    public ResponseEntity<MilesReward> createReward(@RequestBody MilesRewardCreateDto createDto) {
        // We pass the DTO to the service, which handles the ID -> Entity lookup
        MilesReward createdReward = milesRewardService.createReward(createDto);
        return new ResponseEntity<>(createdReward, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MilesReward>> getAllRewards() {
        return ResponseEntity.ok(milesRewardService.getAllRewards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MilesReward> getRewardById(@PathVariable Long id) {
        return ResponseEntity.ok(milesRewardService.getRewardById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MilesReward> updateReward(@PathVariable Long id, @RequestBody MilesRewardCreateDto dto) {
        MilesReward updatedReward = milesRewardService.updateReward(id, dto);
        return ResponseEntity.ok(updatedReward);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable Long id) {
        milesRewardService.deleteReward(id);
        return ResponseEntity.noContent().build(); // Returns HTTP 204 No Content
    }
}