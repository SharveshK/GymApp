package com.gymapp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "medical_conditions")
public class MedicalCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_condition_id")
    private Long medicalConditionId;

    @Column(unique = true, nullable = false)
    private String name; // e.g., "PCOS", "High Blood Pressure", "Knee Pain"

    // This links it back to the 'userProfiles' that have this condition
    @ManyToMany(mappedBy = "medicalConditions")
    private Set<UserProfile> userProfiles = new HashSet<>();
}