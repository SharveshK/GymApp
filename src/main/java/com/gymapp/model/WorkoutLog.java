package com.gymapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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
@Table(name = "workout_logs")
@ToString(exclude = {"user", "workout", "exerciseLogs"})
public class WorkoutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_log_id")
    private Long workoutLogId;

    // Link back to the user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    // Link to the *plan* this log is for
    // It's nullable, in case the user does an 'unplanned' workout.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private Workout workout; // The 'Workout' (plan) entity

    @Column(name = "workout_date")
    private LocalDate workoutDate;

    @Column(name = "user_notes", columnDefinition = "TEXT")
    private String userNotes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // This is the "One" side of the One-to-Many
    // It links to all the ExerciseLog entries for this workout
    @OneToMany(
            mappedBy = "workoutLog",
            cascade = CascadeType.ALL, // If we delete this log, delete all its exercises
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<ExerciseLog> exerciseLogs = new HashSet<>();
}