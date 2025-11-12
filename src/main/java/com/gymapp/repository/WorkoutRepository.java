package com.gymapp.repository;

import com.gymapp.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    /**
     * Finds all workout plans for a user (e.g., for their calendar).
     */
    List<Workout> findByUser_UserIdOrderByWorkoutDateDesc(Long userId);

    /**
     * Finds the *specific* workout plan for a user on a given date.
     * This will be our most-used query.
     */
    Optional<Workout> findByUser_UserIdAndWorkoutDate(Long userId, LocalDate date);

}