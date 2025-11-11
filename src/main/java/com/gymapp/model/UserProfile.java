package com.gymapp.model;

import com.gymapp.model.enums.*;
// Import your other enums here (e.g., ActivityLevel, DietaryType)
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    /**
     * This is the link back to the main Users table.
     * fetch = FetchType.LAZY means it won't load the Users object
     * unless you explicitly ask for it, which is more efficient.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users user;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "height_cm")
    private Double heightCm;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_goal")
    private PrimaryGoal primaryGoal;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level")
    private ExperienceLevel experienceLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "dietary_type")
    private DietaryType dietaryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level")
    private ActivityLevel activityLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_location")
    private WorkoutLocation workoutLocation;

    // ... (inside your UserProfile class)

    /**
     * This is for "The Tickbox" (Many-to-Many).
     * It links to the master list of common equipment.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_profile_equipment",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private Set<Equipment> availableEquipment = new HashSet<>();

    @OneToMany(
            mappedBy = "userProfile",
            cascade = CascadeType.ALL, // If we delete a profile, delete their custom items
            orphanRemoval = true
    )
    private Set<CustomEquipment> customEquipment = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_profile_allergies", // The name of the join table
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "allergy_id")
    )
    private Set<Allergy> allergies = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_profile_medical_conditions", // The name of the join table
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "medical_condition_id")
    )
    private Set<MedicalCondition> medicalConditions = new HashSet<>();

}