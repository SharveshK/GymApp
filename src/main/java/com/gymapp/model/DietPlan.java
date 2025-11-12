package com.gymapp.model;

import com.gymapp.model.enums.DietPlanStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode; // Import this!
import org.hibernate.type.SqlTypes; // Import this!
import lombok.Getter; // NEW
import lombok.Setter; // NEW
import lombok.ToString; // NEW
// import lombok.Data; // DELETE THIS

import java.time.LocalDate;
@Getter // ADD THIS
@Setter // ADD THIS
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diet_plans")
public class DietPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diet_plan_id")
    private Long dietPlanId;

    // Link back to the user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "created_date")
    private LocalDate createdDate;

    // This tells Hibernate to map this String to the JSONB column
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "plan_data", columnDefinition = "jsonb")
    private String planData; // We'll store the plan as a JSON string

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DietPlanStatus status;
}