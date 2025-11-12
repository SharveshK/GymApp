package com.gymapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgressLogRequest {

    // We get user_id from the token, so we only need the data

    private LocalDate logDate; // e.g., "2025-11-12"
    private Double weightKg;
    private Double bodyFatPercentage;
    private String notes;
}