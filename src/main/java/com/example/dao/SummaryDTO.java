package com.example.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Serdeable
public class SummaryDTO {
    private long customerCount;
    private long totalPoints;
}
