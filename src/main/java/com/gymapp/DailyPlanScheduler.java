package com.gymapp;

import com.gymapp.model.UserProfile; // Import this explicitly
import com.gymapp.model.Users;
import com.gymapp.repository.UserRepository;
import com.gymapp.repository.UserProfileRepository;
import com.gymapp.service.AiTriggerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DailyPlanScheduler {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AiTriggerService aiTriggerService;

    public DailyPlanScheduler(UserRepository userRepository,
                              UserProfileRepository userProfileRepository,
                              AiTriggerService aiTriggerService) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.aiTriggerService = aiTriggerService;
    }

    // Runs every day at 3:00 AM server time
    @Scheduled(cron = "0 0 3 * * ?")
    public void generateAllDailyPlans() {
        System.out.println("🕒 Starting Daily Protocol Generation...");

        List<Users> allUsers = userRepository.findAll();

        for (Users user : allUsers) {
            // FIX: Replaced 'var' with 'UserProfile' and used findByUser_UserId
            UserProfile profile = userProfileRepository.findByUser_UserId(user.getUserId()).orElse(null);

            if (profile != null) {
                // Map profile to simple object/map manually here as well
                Map<String, Object> profileMap = mapProfileToMap(profile);

                // Trigger AI
                aiTriggerService.triggerPlanGeneration(user, profileMap);
            }
        }

        System.out.println("✅ Daily Protocol Complete.");
    }

    // Duplicate helper to keep this file independent
    private Map<String, Object> mapProfileToMap(UserProfile p) {
        Map<String, Object> map = new HashMap<>();
        map.put("primaryGoal", p.getPrimaryGoal() != null ? p.getPrimaryGoal().toString() : "MAINTAIN");
        map.put("experienceLevel", p.getExperienceLevel() != null ? p.getExperienceLevel().toString() : "BEGINNER");
        map.put("weightKg", p.getWeightKg());
        map.put("dietaryType", p.getDietaryType() != null ? p.getDietaryType().toString() : "NON_VEG");
        return map;
    }
}