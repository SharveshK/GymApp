package com.gymapp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "allergies")
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allergy_id")
    private Long allergyId;

    @Column(unique = true, nullable = false)
    private String name; // e.g., "Lactose Intolerance", "Peanuts", "Shellfish"

    // This links it back to the 'userProfiles' that have this allergy
    @ManyToMany(mappedBy = "allergies")
    private Set<UserProfile> userProfiles = new HashSet<>();
}