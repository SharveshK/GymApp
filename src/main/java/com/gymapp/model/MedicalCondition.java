package com.gymapp.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter; // NEW
import lombok.Setter; // NEW
import lombok.ToString; // NEW
// import lombok.Data; // DELETE THIS

@Getter // ADD THIS
@Setter // ADD THIS

@Entity
@Table(name = "medical_conditions")
@ToString(exclude = {"users"})
public class MedicalCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "condition_id") // Your SQL uses condition_id
    private Long medicalConditionId;

    @Column(unique = true, nullable = false)
    private String name;

    // --- THIS IS THE FIX ---
    // Change 'mappedBy' to "medicalConditions"
    // Change 'Set<UserProfile> userProfiles' to 'Set<Users> users'
    @ManyToMany(mappedBy = "medicalConditions")
    private Set<Users> users = new HashSet<>();
}