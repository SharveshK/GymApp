package com.gymapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter; // NEW
import lombok.Setter; // NEW
import lombok.ToString; // NEW
// import lombok.Data; // DELETE THIS

@Getter // ADD THIS
@Setter // ADD THIS

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exercise_logs")
@ToString(exclude = {"workoutLog"})
public class ExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_log_id")
    private Long exerciseLogId;

    // This is the "Many" side of the One-to-Many
    // It links *back up* to its "header" WorkoutLog
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_log_id", nullable = false)
    private WorkoutLog workoutLog;

    @Column(name = "exercise_name")
    private String exerciseName;

    @Column(name = "set_number")
    private Integer setNumber;

    @Column(name = "reps_completed")
    private Integer repsCompleted;

    @Column(name = "weight_kg")
    private Double weightKg; // Your SQL is NUMERIC(6,2)

    @Column(name = "rpe")
    private Double rpe; // Your SQL is NUMERIC(3,1)
}