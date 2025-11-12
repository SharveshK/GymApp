package com.gymapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


// This is the main response DTO
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutLogResponse {

    private Long workoutLogId;
    private Long userId;
    private Long workoutPlanId; // The ID of the plan it was for
    private LocalDate workoutDate;
    private String userNotes;
    private LocalDateTime createdAt;

    // The nested list of saved exercises
    private List<ExerciseLogResponse> exerciseLogs;
}