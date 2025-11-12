package com.gymapp.controller;

import com.gymapp.dto.DietPlanRequest;
import com.gymapp.dto.DietPlanResponse;
import com.gymapp.service.DietPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diet-plans") // Base URL for all diet plan endpoints
public class DietPlanController {

    private final DietPlanService dietPlanService;

    // Manual Constructor (as requested)
    public DietPlanController(DietPlanService dietPlanService) {
        this.dietPlanService = dietPlanService;
    }

    /**
     * GET /api/v1/diet-plans/active
     * Gets the *currently active* diet plan for the authenticated user.
     * The user is identified by their JWT.
     */
    @GetMapping("/active")
    public ResponseEntity<DietPlanResponse> getActiveDietPlanForCurrentUser() {
        DietPlanResponse plan = dietPlanService.getActivePlanForCurrentUser();
        return ResponseEntity.ok(plan); // Will return 'null' if no active plan
    }

    /**
     * GET /api/v1/diet-plans/all
     * Gets *all* diet plans (active and archived) for the authenticated user.
     * The user is identified by their JWT.
     */
    @GetMapping("/all")
    public ResponseEntity<List<DietPlanResponse>> getAllDietPlansForCurrentUser() {
        List<DietPlanResponse> plans = dietPlanService.getAllPlansForCurrentUser();
        return ResponseEntity.ok(plans);
    }

    /**
     * POST /api/v1/diet-plans
     * Creates a new diet plan. This endpoint is intended to be called
     * by your AI service or an Admin.
     *
     * (In the future, we would add @PreAuthorize("hasRole('ADMIN')") to
     * secure this endpoint, but for now, it's just a private endpoint).
     */
    @PostMapping
    public ResponseEntity<DietPlanResponse> createDietPlan(
            @RequestBody DietPlanRequest request
    ) {
        DietPlanResponse newPlan = dietPlanService.createDietPlan(request);
        // Return 201 Created status
        return new ResponseEntity<>(newPlan, HttpStatus.CREATED);
    }
}