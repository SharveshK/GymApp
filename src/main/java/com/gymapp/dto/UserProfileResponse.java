package com.gymapp.dto;

import com.gymapp.model.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

    // Basic Info
    private Long userId;
    private String email;

    // Physical Stats
    private LocalDate dateOfBirth;
    private Double heightCm;
    private Double weightKg;
    private Double bodyFatPercentage;
    private Gender gender;

    // Goals & Lifestyle
    private PrimaryGoal primaryGoal;
    private ExperienceLevel experienceLevel;
    private DietaryType dietaryType;
    private ActivityLevel activityLevel;
    private WorkoutLocation workoutLocation;
    private Integer mealFrequency;
    private Integer cookingTimeMinutes;

    // The Lists (We just send the names, easy for AI to read)
    private Set<String> allergies;
    private Set<String> medicalConditions;
    private Set<String> equipment;
    private Set<String> dislikedFoods;
    private Set<String> customEquipment;
}