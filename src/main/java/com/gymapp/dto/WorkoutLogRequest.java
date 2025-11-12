package com.gymapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutLogRequest {

    // We get user_id from the token

    private LocalDate workoutDate;
    private String userNotes;

    // This ID is optional. It's used to link this log
    // back to the AI's *plan* (from the 'workouts' table).
    private Long workoutPlanId; // This is the 'workout_id'

    // This is the nested list of exercises
    private List<ExerciseLogRequest> exerciseLogs;
}