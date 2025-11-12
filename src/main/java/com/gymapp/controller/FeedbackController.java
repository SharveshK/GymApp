package com.gymapp.controller;

import com.gymapp.dto.FeedbackRequest;
import com.gymapp.dto.FeedbackResponse;
import com.gymapp.service.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback") // Base URL for all feedback endpoints
public class FeedbackController {

    private final FeedbackService feedbackService;

    // Manual Constructor (as requested)
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * POST /api/v1/feedback
     * Creates a new feedback entry for the authenticated user.
     */
    @PostMapping
    public ResponseEntity<FeedbackResponse> createFeedback(
            @RequestBody FeedbackRequest request
    ) {
        FeedbackResponse response = feedbackService.createFeedback(request);
        // Return 201 Created status, as this is always a new resource
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/feedback
     * Gets all feedback entries for the authenticated user.
     */
    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAllFeedback() {
        List<FeedbackResponse> feedbackList = feedbackService.getAllFeedbackForCurrentUser();
        return ResponseEntity.ok(feedbackList);
    }
}