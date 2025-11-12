package com.gymapp.repository;

import com.gymapp.model.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {

    // Find all logs for a user (for history)
    List<WorkoutLog> findByUser_UserIdOrderByWorkoutDateDesc(Long userId);

    // Find all logs for a user on a specific date
    List<WorkoutLog> findByUser_UserIdAndWorkoutDate(Long userId, LocalDate date);
}