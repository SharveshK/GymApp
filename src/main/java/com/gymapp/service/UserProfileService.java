package com.gymapp.service;

import com.gymapp.dto.UserProfileResponse;
import com.gymapp.model.Users;
import com.gymapp.model.UserProfile;
import com.gymapp.repository.UserProfileRepository;
import com.gymapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getFullProfile() {
        // 1. Get the logged-in user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Get the profile details
        UserProfile profile = userProfileRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // 3. Build the huge response object
        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .dateOfBirth(profile.getDateOfBirth())
                .heightCm(profile.getHeightCm())
                .weightKg(profile.getWeightKg())
                .bodyFatPercentage(profile.getBodyFatPercentage())
                .gender(profile.getGender())
                .primaryGoal(profile.getPrimaryGoal())
                .experienceLevel(profile.getExperienceLevel())
                .dietaryType(profile.getDietaryType())
                .activityLevel(profile.getActivityLevel())
                .workoutLocation(profile.getWorkoutLocation())
                .mealFrequency(profile.getMealFrequency())
                .cookingTimeMinutes(profile.getCookingTimeMinutes())
                // Map the lists to Strings
                .allergies(user.getAllergies().stream().map(a -> a.getName()).collect(Collectors.toSet()))
                .medicalConditions(user.getMedicalConditions().stream().map(m -> m.getName()).collect(Collectors.toSet()))
                .equipment(user.getAvailableEquipment().stream().map(e -> e.getName()).collect(Collectors.toSet()))
                .dislikedFoods(user.getDislikedFoods().stream().map(f -> f.getName()).collect(Collectors.toSet()))
                .customEquipment(user.getCustomEquipment().stream().map(c -> c.getName()).collect(Collectors.toSet()))
                .build();
    }
}