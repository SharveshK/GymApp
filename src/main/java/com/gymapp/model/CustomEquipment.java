package com.gymapp.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Getter; // NEW
import lombok.Setter; // NEW
import lombok.ToString; // NEW
// import lombok.Data; // DELETE THIS

@Getter // ADD THIS
@Setter // ADD THIS

@NoArgsConstructor
@Entity
@Table(name = "custom_equipment")
public class CustomEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_equipment_id")
    private Long customEquipmentId;

    // --- THIS IS THE FIX ---
    // Change 'UserProfile userProfile' to 'Users user'
    // Update @JoinColumn to point to "user_id"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(nullable = false)
    private String name;
}