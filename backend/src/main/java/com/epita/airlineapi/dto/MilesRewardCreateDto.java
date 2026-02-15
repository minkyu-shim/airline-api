package com.epita.airlineapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MilesRewardCreateDto {
    @NotNull(message = "clientId is required")
    private Long clientId;

    @NotNull(message = "flightId is required")
    private Long flightId;

    @NotNull(message = "date is required")
    private LocalDate date;
}
