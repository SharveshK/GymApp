package com.gymapp.service;

import com.gymapp.dto.ExerciseLogRequest;
import com.gymapp.dto.WorkoutLogRequest;
import com.gymapp.dto.WorkoutLogResponse;
import com.gymapp.model.ExerciseLog;
import com.gymapp.model.Users;
import com.gymapp.model.Workout;
import com.gymapp.model.WorkoutLog;
import com.gymapp.model.enums.WorkoutStatus;
import com.gymapp.repository.WorkoutLogRepository;
import com.gymapp.repository.WorkoutRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// We need to import the DTO for the response
import com.gymapp.dto.ExerciseLogResponse;


@Service
public class WorkoutLogService {

    private final WorkoutLogRepository workoutLogRepository;
    private final WorkoutRepository workoutRepository; // For updating the plan status

    // Manual Constructor (as requested)
    public WorkoutLogService(WorkoutLogRepository workoutLogRepository, WorkoutRepository workoutRepository) {
        this.workoutLogRepository = workoutLogRepository;
        this.workoutRepository = workoutRepository;
    }

    /**
     * Creates a new, complete workout log (header + details)
     * for the *currently logged-in user*.
     */
    @Transactional
    public WorkoutLogResponse createWorkoutLog(WorkoutLogRequest request) {
        // 1. Get the current user
        Users currentUser = getAuthenticatedUser();

        // 2. Create the "Header" (WorkoutLog)
        WorkoutLog workoutLog = WorkoutLog.builder()
                .user(currentUser)
                .workoutDate(request.getWorkoutDate())
                .userNotes(request.getUserNotes())
                .build();

        // 3. Link to the *Plan* (if an ID was provided)
        //    and update the plan's status to COMPLETED
        if (request.getWorkoutPlanId() != null) {
            workoutRepository.findById(request.getWorkoutPlanId())
                    .ifPresent(workoutPlan -> {
                        // Check if plan belongs to this user (for security)
                        if (!workoutPlan.getUser().getUserId().equals(currentUser.getUserId())) {
                            throw new SecurityException("User cannot log against a plan that is not theirs.");
                        }
                        workoutLog.setWorkout(workoutPlan); // Link the log to the plan
                        workoutPlan.setStatus(WorkoutStatus.COMPLETED); // Mark plan as done
                        workoutRepository.save(workoutPlan);
                    });
        }

        // 4. Create the "Details" (ExerciseLogs)
        if (request.getExerciseLogs() != null) {
            request.getExerciseLogs().stream()
                    .map(exLogReq -> mapRequestToExerciseLog(exLogReq, workoutLog))
                    .forEach(exLog -> workoutLog.getExerciseLogs().add(exLog));
        }

        // 5. Save everything in one transaction
        // Thanks to CascadeType.ALL, saving the 'workoutLog'
        // will also save all its 'exerciseLogs'.
        WorkoutLog savedLog = workoutLogRepository.save(workoutLog);

        // 6. Return the full, nested response
        return mapEntityToResponse(savedLog);
    }

    /**
     * Gets all workout logs (with details) for the *currently logged-in user*
     * for a *specific date*.
     */
    @Transactional(readOnly = true)
    public List<WorkoutLogResponse> getWorkoutLogsForDate(LocalDate date) {
        Users currentUser = getAuthenticatedUser();
        return workoutLogRepository.findByUser_UserIdAndWorkoutDate(currentUser.getUserId(), date)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    // --- HELPER METHODS ---

    private Users getAuthenticatedUser() {
        return (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // Maps the child DTO (ExerciseLogRequest) to the child Entity (ExerciseLog)
    private ExerciseLog mapRequestToExerciseLog(ExerciseLogRequest req, WorkoutLog parentLog) {
        return ExerciseLog.builder()
                .workoutLog(parentLog) // Link to the parent
                .exerciseName(req.getExerciseName())
                .setNumber(req.getSetNumber())
                .repsCompleted(req.getRepsCompleted())
                .weightKg(req.getWeightKg())
                .rpe(req.getRpe())
                .build();
    }

    // Maps the parent Entity (WorkoutLog) to the parent DTO (WorkoutLogResponse)
    private WorkoutLogResponse mapEntityToResponse(WorkoutLog log) {

        // Map all the child entities (ExerciseLog) to child DTOs (ExerciseLogResponse)
        List<ExerciseLogResponse> exLogResponses = log.getExerciseLogs().stream()
                .map(exLog -> ExerciseLogResponse.builder()
                        .exerciseLogId(exLog.getExerciseLogId())
                        .exerciseName(exLog.getExerciseName())
                        .setNumber(exLog.getSetNumber())
                        .repsCompleted(exLog.getRepsCompleted())
                        .weightKg(exLog.getWeightKg())
                        .rpe(exLog.getRpe())
                        .build())
                .collect(Collectors.toList());

        // Build the main response DTO
        return WorkoutLogResponse.builder()
                .workoutLogId(log.getWorkoutLogId())
                .userId(log.getUser().getUserId())
                .workoutPlanId(log.getWorkout() != null ? log.getWorkout().getWorkoutId() : null)
                .workoutDate(log.getWorkoutDate())
                .userNotes(log.getUserNotes())
                .createdAt(log.getCreatedAt())
                .exerciseLogs(exLogResponses) // Add the nested list
                .build();
    }
}