package com.gymapp.dto;

import com.gymapp.model.enums.WorkoutStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutResponse {

    private Long workoutId;
    private Long userId;
    private LocalDate workoutDate;
    private WorkoutStatus status;
    private String workoutData; // The JSON string of the plan
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}