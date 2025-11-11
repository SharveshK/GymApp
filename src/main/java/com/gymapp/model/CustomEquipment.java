package com.gymapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "custom_equipment")
public class CustomEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_equipment_id")
    private Long customEquipmentId;

    // This links it back to the UserProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private UserProfile userProfile;

    // The custom name the user typed in
    @Column(nullable = false)
    private String name; // e.g., "20kg Kettlebell", "TRX Straps"

    public CustomEquipment(UserProfile userProfile, String name) {
        this.userProfile = userProfile;
        this.name = name;
    }
}