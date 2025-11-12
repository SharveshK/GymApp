package com.gymapp.repository;

import com.gymapp.model.ExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {

    // Find all exercises for a specific workout log
    List<ExerciseLog> findByWorkoutLog_WorkoutLogId(Long workoutLogId);
}