package com.gymapp.controller;

import com.gymapp.dto.WorkoutRequest;
import com.gymapp.dto.WorkoutResponse;
import com.gymapp.service.WorkoutService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/workouts") // Base URL for all workout endpoints
public class WorkoutController {

    private final WorkoutService workoutService;

    // Manual Constructor (as requested)
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    /**
     * POST /api/v1/workouts
     * Creates (or updates) a workout plan for a specific user and date.
     * Intended to be called by the AI service.
     */
    @PostMapping
    public ResponseEntity<WorkoutResponse> createOrUpdateWorkout(
            @RequestBody WorkoutRequest request
    ) {
        WorkoutResponse response = workoutService.createOrUpdateWorkout(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/workouts
     * Gets the workout plan for the authenticated user for a *specific date*.
     * Example: /api/v1/workouts?date=2025-11-14
     */
    @GetMapping
    public ResponseEntity<WorkoutResponse> getWorkoutForDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        WorkoutResponse response = workoutService.getWorkoutForDate(date);
        return ResponseEntity.ok(response); // Returns 200 OK (with null body) if no plan
    }

    /**
     * GET /api/v1/workouts/all
     * Gets all workout plans (history) for the authenticated user.
     */
    @GetMapping("/all")
    public ResponseEntity<List<WorkoutResponse>> getAllWorkouts() {
        List<WorkoutResponse> response = workoutService.getAllWorkoutsForCurrentUser();
        return ResponseEntity.ok(response);
    }
}