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
public class ProgressLogResponse {

    private Long logId;
    private Long userId;
    private LocalDate logDate;
    private Double weightKg;
    private Double bodyFatPercentage;
    private String notes;
}