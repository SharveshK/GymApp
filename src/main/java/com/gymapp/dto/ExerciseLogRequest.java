package com.gymapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseLogRequest {

    // No IDs needed, this is part of a larger object

    private String exerciseName;
    private Integer setNumber;
    private Integer repsCompleted;
    private Double weightKg;
    private Double rpe; // Rate of Perceived Exertion
}