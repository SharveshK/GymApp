package com.gymapp.controller;

import com.gymapp.dto.ProgressLogRequest;
import com.gymapp.dto.ProgressLogResponse;
import com.gymapp.service.ProgressLogsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/progress-logs") // Base URL for all progress log endpoints
public class ProgressLogsController {

    private final ProgressLogsService progressLogsService;

    // Manual Constructor (as requested)
    public ProgressLogsController(ProgressLogsService progressLogsService) {
        this.progressLogsService = progressLogsService;
    }

    /**
     * GET /api/v1/progress-logs
     * Gets all progress logs for the currently authenticated user.
     * The user is identified by their JWT.
     */
    @GetMapping
    public ResponseEntity<List<ProgressLogResponse>> getAllLogsForUser() {
        List<ProgressLogResponse> logs = progressLogsService.getAllLogsForCurrentUser();
        return ResponseEntity.ok(logs);
    }

    /**
     * POST /api/v1/progress-logs
     * Creates a new log or updates an existing one for the given date.
     * The user is identified by their JWT.
     */
    @PostMapping
    public ResponseEntity<ProgressLogResponse> createOrUpdateLog(
            @RequestBody ProgressLogRequest request
    ) {
        ProgressLogResponse response = progressLogsService.createOrUpdateLog(request);
        // Return 201 CREATED if it's a new entry, 200 OK if it was an update.
        // For simplicity, we can just return 200 OK for this upsert logic.
        return ResponseEntity.ok(response);
    }
}