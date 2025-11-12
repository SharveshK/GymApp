package com.gymapp.service;

import com.gymapp.dto.DietPlanRequest;
import com.gymapp.dto.DietPlanResponse;
import com.gymapp.model.DietPlan;
import com.gymapp.model.Users;
import com.gymapp.model.enums.DietPlanStatus;
import com.gymapp.repository.DietPlanRepository;
import com.gymapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietPlanService {

    private final DietPlanRepository dietPlanRepository;
    private final UserRepository userRepository; // We need this to find the user

    // Manual Constructor (as requested)
    public DietPlanService(DietPlanRepository dietPlanRepository, UserRepository userRepository) {
        this.dietPlanRepository = dietPlanRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new diet plan for a specific user.
     * This will also archive any existing 'ACTIVE' plan for that user.
     */
    @Transactional
    public DietPlanResponse createDietPlan(DietPlanRequest request) {
        // 1. Find the user this plan is for
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        // 2. Find and archive any existing 'ACTIVE' plan
        dietPlanRepository.findByUser_UserIdAndStatus(user.getUserId(), DietPlanStatus.ACTIVE)
                .ifPresent(oldPlan -> {
                    oldPlan.setStatus(DietPlanStatus.ARCHIVED);
                    dietPlanRepository.save(oldPlan);
                });

        // 3. Create the new plan
        DietPlan newPlan = DietPlan.builder()
                .user(user)
                .createdDate(LocalDate.now())
                .planData(request.getPlanData())
                .status(DietPlanStatus.ACTIVE) // Set the new plan as ACTIVE
                .build();

        // 4. Save and return the new plan
        DietPlan savedPlan = dietPlanRepository.save(newPlan);
        return mapEntityToResponse(savedPlan);
    }

    /**
     * Gets the *current active* diet plan for the *currently logged-in user*.
     */
    @Transactional(readOnly = true)
    public DietPlanResponse getActivePlanForCurrentUser() {
        Users currentUser = getAuthenticatedUser();

        return dietPlanRepository.findByUser_UserIdAndStatus(currentUser.getUserId(), DietPlanStatus.ACTIVE)
                .map(this::mapEntityToResponse) // .map() on an Optional is cleaner
                .orElse(null); // Return null (or throw exception) if no active plan
    }

    /**
     * Gets all diet plans (active and archived) for the *currently logged-in user*.
     */
    @Transactional(readOnly = true)
    public List<DietPlanResponse> getAllPlansForCurrentUser() {
        Users currentUser = getAuthenticatedUser();

        return dietPlanRepository.findByUser_UserIdOrderByCreatedDateDesc(currentUser.getUserId())
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    // --- HELPER METHODS ---

    private Users getAuthenticatedUser() {
        return (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private DietPlanResponse mapEntityToResponse(DietPlan plan) {
        return DietPlanResponse.builder()
                .dietPlanId(plan.getDietPlanId())
                .userId(plan.getUser().getUserId())
                .createdDate(plan.getCreatedDate())
                .status(plan.getStatus())
                .planData(plan.getPlanData())
                .build();
    }
}