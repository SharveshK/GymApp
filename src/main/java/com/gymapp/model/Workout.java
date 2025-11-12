package com.gymapp.model;

import com.gymapp.model.enums.WorkoutStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import lombok.Getter; // NEW
import lombok.Setter; // NEW
import lombok.ToString; // NEW
// import lombok.Data; // DELETE THIS

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter // ADD THIS
@Setter // ADD THIS


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workouts")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_id")
    private Long workoutId;

    // Link back to the user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "workout_date")
    private LocalDate workoutDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WorkoutStatus status;

    // This maps our String field to the JSONB column
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "workout_data", columnDefinition = "jsonb")
    private String workoutData; // We'll store the plan as a JSON string

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}