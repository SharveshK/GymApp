package com.gymapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
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
@Table(name = "progress_logs", uniqueConstraints = {
        // This matches the UNIQUE constraint we set in our SQL
        @UniqueConstraint(columnNames = {"user_id", "log_date"})
})
public class ProgressLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    // This is the link back to the Users table
    // FetchType.LAZY is efficient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "body_fat_percentage")
    private Double bodyFatPercentage;

    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}