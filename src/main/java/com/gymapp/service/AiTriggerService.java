package com.gymapp.service;

import com.gymapp.dto.UserProfileResponse; // You might need to create a DTO for this
import com.gymapp.model.Users;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Async;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiTriggerService {

    private final String PYTHON_URL = "http://localhost:5000/generate-user-plan";
    private final RestTemplate restTemplate = new RestTemplate();

    @Async // Run in background so the user doesn't wait on the Register screen
    public void triggerPlanGeneration(Users user, Object profileData) {
        try {
            System.out.println("🤖 Signaling AI to generate plan for User: " + user.getEmail());

            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", user.getUserId());
            payload.put("profileData", profileData); // Send the profile we just saved

            restTemplate.postForEntity(PYTHON_URL, payload, String.class);

        } catch (Exception e) {
            System.err.println("❌ AI Trigger Failed: " + e.getMessage());
        }
    }
}