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
@Table(name = "equipment")
@ToString(exclude = {"users"})
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Long equipmentId;

    @Column(unique = true, nullable = false)
    private String name;

    // --- THIS IS THE FIX ---
    // Change 'mappedBy' to "availableEquipment"
    // Change 'Set<UserProfile> userProfiles' to 'Set<Users> users'
    @ManyToMany(mappedBy = "availableEquipment")
    private Set<Users> users = new HashSet<>();
}