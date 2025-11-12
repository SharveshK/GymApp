package com.gymapp.dto;

import com.gymapp.model.enums.DietPlanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietPlanResponse {

    private Long dietPlanId;
    private Long userId;
    private LocalDate createdDate;
    private DietPlanStatus status;

    // We can return the planData as a String, or parse it.
    // For now, returning the string is fine.
    private String planData;
}