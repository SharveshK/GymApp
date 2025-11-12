package com.gymapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietPlanRequest {

    // We *do* need the userId here, as the "AI" is creating this
    // plan *for* a specific user.
    private Long userId;

    /**
     * The full diet plan as a JSON string.
     * Example:
     * {
     * "dailyCalories": 2500,
     * "macros": {"protein": 180, "carbs": 250, "fat": 70},
     * "meals": [
     * {"meal": "Breakfast", "food": "Oats, Whey, Berries"},
     * {"meal": "Lunch", "food": "Chicken Breast, Rice, Broccoli"}
     * ]
     * }
     */
    private String planData;
}