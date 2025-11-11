package com.gymapp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Long equipmentId;

    @Column(unique = true, nullable = false)
    private String name; // e.g., "Dumbbells", "Adjustable Bench"

    // This maps it back to the UserProfile
    @ManyToMany(mappedBy = "availableEquipment")
    private Set<UserProfile> userProfiles = new HashSet<>();
}