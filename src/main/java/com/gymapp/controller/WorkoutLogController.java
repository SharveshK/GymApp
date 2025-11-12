package com.gymapp.controller;

import com.gymapp.dto.WorkoutLogRequest;
import com.gymapp.dto.WorkoutLogResponse;
import com.gymapp.service.WorkoutLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/workout-logs") // Base URL for all workout log endpoints
public class WorkoutLogController {

    private final WorkoutLogService workoutLogService;

    // Manual Constructor (as requested)
    public WorkoutLogController(WorkoutLogService workoutLogService) {
        this.workoutLogService = workoutLogService;
    }

    /**
     * POST /api/v1/workout-logs
     * Creates a new, complete workout log (with all exercises)
     * for the currently authenticated user.
     */
    @PostMapping
    public ResponseEntity<WorkoutLogResponse> createWorkoutLog(
            @RequestBody WorkoutLogRequest request
    ) {
        // The service handles all the complex logic of saving
        // the header and all the child exercises.
        WorkoutLogResponse response = workoutLogService.createWorkoutLog(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/workout-logs/by-date
     * Gets all workout logs for the authenticated user for a specific date.
     * Example: /api/v1/workout-logs/by-date?date=2025-11-13
     */
    @GetMapping("/by-date")
    public ResponseEntity<List<WorkoutLogResponse>> getWorkoutLogsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<WorkoutLogResponse> logs = workoutLogService.getWorkoutLogsForDate(date);
        return ResponseEntity.ok(logs);
    }
}