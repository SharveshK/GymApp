package com.gymapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseLogResponse {

    // This is the "child" DTO for our nested response

    private Long exerciseLogId;
    private String exerciseName;
    private Integer setNumber;
    private Integer repsCompleted;
    private Double weightKg;
    private Double rpe;
}