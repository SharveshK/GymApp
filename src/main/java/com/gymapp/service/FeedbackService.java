package com.gymapp.service;

import com.gymapp.dto.FeedbackRequest;
import com.gymapp.dto.FeedbackResponse;
import com.gymapp.model.Feedback;
import com.gymapp.model.Users;
import com.gymapp.repository.FeedbackRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    // Manual Constructor (as requested)
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    /**
     * Creates a new feedback entry for the *currently logged-in user*.
     */
    @Transactional
    public FeedbackResponse createFeedback(FeedbackRequest request) {
        // Tip #1: Get the user from the security token
        Users currentUser = getAuthenticatedUser();

        // Build the new Feedback entity
        Feedback newFeedback = Feedback.builder()
                .user(currentUser)
                .feedbackText(request.getFeedbackText())
                .isSummarized(false) // Default value
                .build();

        // Save it to the database
        Feedback savedFeedback = feedbackRepository.save(newFeedback);

        // Map and return the DTO
        return mapEntityToResponse(savedFeedback);
    }

    /**
     * Gets all feedback for the *currently logged-in user*.
     */
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getAllFeedbackForCurrentUser() {
        Users currentUser = getAuthenticatedUser();

        return feedbackRepository.findByUser_UserIdOrderByCreatedAtDesc(currentUser.getUserId())
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    // --- HELPER METHODS ---

    /**
     * Gets the User object from the security token.
     */
    private Users getAuthenticatedUser() {
        return (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Maps the database Entity to the API Response DTO.
     */
    private FeedbackResponse mapEntityToResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .feedbackId(feedback.getFeedbackId())
                .userId(feedback.getUser().getUserId())
                .feedbackText(feedback.getFeedbackText())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}