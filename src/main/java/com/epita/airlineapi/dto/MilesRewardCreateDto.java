package com.epita.airlineapi.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MilesRewardCreateDto {
    private Long clientId;
    private Long flightId;
    private LocalDate date;
}