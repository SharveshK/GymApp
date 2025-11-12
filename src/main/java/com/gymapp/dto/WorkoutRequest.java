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
public class WorkoutRequest {

    // The user this plan is for
    private Long userId;

    // The date this workout is scheduled for
    private LocalDate workoutDate;

    /**
     * The full workout plan as a JSON string.
     * Example:
     * {
     * "name": "Heavy Chest Day",
     * "exercises": [
     * {"exercise": "Bench Press", "plan": "3 sets x 5 reps @ 80kg"},
     * {"exercise": "Incline DB Press", "plan": "3 sets x 8 reps @ 30kg"}
     * ]
     * }
     */
    private String workoutData;
}