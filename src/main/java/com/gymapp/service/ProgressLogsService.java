package com.gymapp.service;

import com.gymapp.dto.ProgressLogRequest;
import com.gymapp.dto.ProgressLogResponse;
import com.gymapp.model.ProgressLogs;
import com.gymapp.model.Users;
import com.gymapp.repository.ProgressLogsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgressLogsService {

    private final ProgressLogsRepository progressLogsRepository;

    // Manual Constructor (as requested)
    public ProgressLogsService(ProgressLogsRepository progressLogsRepository) {
        this.progressLogsRepository = progressLogsRepository;
    }

    /**
     * Gets all progress logs for the *currently logged-in user*.
     */
    @Transactional(readOnly = true)
    public List<ProgressLogResponse> getAllLogsForCurrentUser() {
        Users currentUser = getAuthenticatedUser();

        // --- THIS IS FIX #1 ---
        // We are calling the new repository method name
        return progressLogsRepository.findByUser_UserIdOrderByLogDateAsc(currentUser.getUserId())
                .stream()
                .map(this::mapEntityToResponse) // Helper method to convert
                .collect(Collectors.toList());
    }

    /**
     * Creates or updates a progress log for the *currently logged-in user*.
     * This uses an "upsert" logic.
     */
    @Transactional
    public ProgressLogResponse createOrUpdateLog(ProgressLogRequest request) {
        Users currentUser = getAuthenticatedUser();

        // --- THIS IS FIX #2 ---
        // We are calling the new repository method name
        Optional<ProgressLogs> existingLogOpt =
                progressLogsRepository.findByUser_UserIdAndLogDate(currentUser.getUserId(), request.getLogDate());

        ProgressLogs logToSave;

        if (existingLogOpt.isPresent()) {
            // --- UPDATE ---
            logToSave = existingLogOpt.get();
            logToSave.setWeightKg(request.getWeightKg());
            logToSave.setBodyFatPercentage(request.getBodyFatPercentage());
            logToSave.setNotes(request.getNotes());
        } else {
            // --- CREATE ---
            logToSave = ProgressLogs.builder()
                    .user(currentUser)
                    .logDate(request.getLogDate())
                    .weightKg(request.getWeightKg())
                    .bodyFatPercentage(request.getBodyFatPercentage())
                    .notes(request.getNotes())
                    .build();
        }

        ProgressLogs savedLog = progressLogsRepository.save(logToSave);
        return mapEntityToResponse(savedLog);
    }

    // --- HELPER METHODS ---

    private Users getAuthenticatedUser() {
        return (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private ProgressLogResponse mapEntityToResponse(ProgressLogs log) {
        return ProgressLogResponse.builder()
                .logId(log.getLogId())
                .userId(log.getUser().getUserId())
                .logDate(log.getLogDate())
                .weightKg(log.getWeightKg())
                .bodyFatPercentage(log.getBodyFatPercentage())
                .notes(log.getNotes())
                .build();
    }
}