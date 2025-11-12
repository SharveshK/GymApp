package com.gymapp.service;

import com.gymapp.dto.WorkoutRequest;
import com.gymapp.dto.WorkoutResponse;
import com.gymapp.model.Users;
import com.gymapp.model.Workout;
import com.gymapp.model.enums.WorkoutStatus;
import com.gymapp.repository.UserRepository;
import com.gymapp.repository.WorkoutRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    // Manual Constructor (as requested)
    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new workout plan or updates an existing one
     * for a specific user and date.
     */
    @Transactional
    public WorkoutResponse createOrUpdateWorkout(WorkoutRequest request) {
        // 1. Find the user this plan is for
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        // 2. Check if a plan for this user and date already exists
        Optional<Workout> existingWorkoutOpt =
                workoutRepository.findByUser_UserIdAndWorkoutDate(user.getUserId(), request.getWorkoutDate());

        Workout workoutToSave;

        if (existingWorkoutOpt.isPresent()) {
            // --- UPDATE Existing Plan ---
            workoutToSave = existingWorkoutOpt.get();
            workoutToSave.setWorkoutData(request.getWorkoutData());
            // Reset status in case it was 'SKIPPED'
            workoutToSave.setStatus(WorkoutStatus.PENDING);
        } else {
            // --- CREATE New Plan ---
            workoutToSave = Workout.builder()
                    .user(user)
                    .workoutDate(request.getWorkoutDate())
                    .workoutData(request.getWorkoutData())
                    .status(WorkoutStatus.PENDING)
                    .build();
        }

        // 3. Save and return
        Workout savedWorkout = workoutRepository.save(workoutToSave);
        return mapEntityToResponse(savedWorkout);
    }

    /**
     * Gets the workout plan for the *currently logged-in user*
     * for a *specific date*.
     */
    @Transactional(readOnly = true)
    public WorkoutResponse getWorkoutForDate(LocalDate date) {
        Users currentUser = getAuthenticatedUser();

        return workoutRepository.findByUser_UserIdAndWorkoutDate(currentUser.getUserId(), date)
                .map(this::mapEntityToResponse)
                .orElse(null); // Return null if no workout is scheduled
    }

    /**
     * Gets all workout plans for the *currently logged-in user*.
     */
    @Transactional(readOnly = true)
    public List<WorkoutResponse> getAllWorkoutsForCurrentUser() {
        Users currentUser = getAuthenticatedUser();

        return workoutRepository.findByUser_UserIdOrderByWorkoutDateDesc(currentUser.getUserId())
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    // --- HELPER METHODS ---

    private Users getAuthenticatedUser() {
        return (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private WorkoutResponse mapEntityToResponse(Workout workout) {
        return WorkoutResponse.builder()
                .workoutId(workout.getWorkoutId())
                .userId(workout.getUser().getUserId())
                .workoutDate(workout.getWorkoutDate())
                .status(workout.getStatus())
                .workoutData(workout.getWorkoutData())
                .createdAt(workout.getCreatedAt())
                .updatedAt(workout.getUpdatedAt())
                .build();
    }
}