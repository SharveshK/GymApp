package com.gymapp.repository;

import com.gymapp.model.DietPlan;
import com.gymapp.model.enums.DietPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {

    /**
     * Finds all diet plans for a user (e.g., to show their history).
     */
    List<DietPlan> findByUser_UserIdOrderByCreatedDateDesc(Long userId);

    /**
     * Finds the *current active* diet plan for a user.
     * We'll use this to make sure a user only has one active plan.
     */
    Optional<DietPlan> findByUser_UserIdAndStatus(Long userId, DietPlanStatus status);

}