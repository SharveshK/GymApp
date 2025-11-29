package com.gymapp.service;

import com.gymapp.dto.AuthResponse;
import com.gymapp.dto.LoginRequest;
import com.gymapp.dto.RegisterRequest;
import com.gymapp.model.*;
import com.gymapp.repository.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AiTriggerService aiTriggerService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final UserProfileRepository userProfileRepository;
    private final AllergyRepository allergyRepository;
    private final MedicalConditionRepository medicalConditionRepository;
    private final EquipmentRepository equipmentRepository;
    private final FoodRepository foodRepository;

    public AuthService(UserRepository userRepository,
                       AiTriggerService aiTriggerService,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       UserProfileRepository userProfileRepository,
                       AllergyRepository allergyRepository,
                       MedicalConditionRepository medicalConditionRepository,
                       EquipmentRepository equipmentRepository,
                       FoodRepository foodRepository) {
        this.userRepository = userRepository;
        this.aiTriggerService = aiTriggerService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userProfileRepository = userProfileRepository;
        this.allergyRepository = allergyRepository;
        this.medicalConditionRepository = medicalConditionRepository;
        this.equipmentRepository = equipmentRepository;
        this.foodRepository = foodRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthResponse register(RegisterRequest request) {

        // 1. Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already taken");
        }

        // 2. Fetch Lists
        Set<Allergy> allergies = new HashSet<>();
        if (request.getAllergyIds() != null) {
            allergies = new HashSet<>(allergyRepository.findAllById(request.getAllergyIds()));
        }

        Set<MedicalCondition> medicalConditions = new HashSet<>();
        if (request.getMedicalConditionIds() != null) {
            medicalConditions = new HashSet<>(medicalConditionRepository.findAllById(request.getMedicalConditionIds()));
        }

        Set<Equipment> equipment = new HashSet<>();
        if (request.getEquipmentIds() != null) {
            equipment = new HashSet<>(equipmentRepository.findAllById(request.getEquipmentIds()));
        }

        Set<Food> dislikedFoods = new HashSet<>();
        if (request.getDislikedFoodIds() != null) {
            dislikedFoods = new HashSet<>(foodRepository.findAllById(request.getDislikedFoodIds()));
        }

        // 3. Create User
        Users user = Users.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))// if you did, remove these two lines
                .allergies(allergies)
                .medicalConditions(medicalConditions)
                .availableEquipment(equipment)
                .dislikedFoods(dislikedFoods)
                .build();

        // 4. Handle Custom Equipment
        if (request.getCustomEquipmentNames() != null) {
            request.getCustomEquipmentNames().forEach(name -> {
                CustomEquipment ce = new CustomEquipment();
                ce.setUser(user);
                ce.setName(name);
                user.getCustomEquipment().add(ce);
            });
        }

        // 5. Save User
        Users savedUser = userRepository.save(user);

        // 6. Create Profile
        UserProfile userProfile = UserProfile.builder()
                .user(savedUser)
                .dateOfBirth(request.getDateOfBirth())
                .heightCm(request.getHeightCm())
                .weightKg(request.getWeightKg())
                .bodyFatPercentage(request.getBodyFatPercentage())
                .gender(request.getGender())
                .primaryGoal(request.getPrimaryGoal())
                .experienceLevel(request.getExperienceLevel())
                .dietaryType(request.getDietaryType())
                .activityLevel(request.getActivityLevel())
                .workoutLocation(request.getWorkoutLocation())
                .mealFrequency(request.getMealFrequency())
                .cookingTimeMinutes(request.getCookingTimeMinutes())
                .build();

        // FIX: Capture the saved profile into a variable
        UserProfile savedProfile = userProfileRepository.save(userProfile);

        // 7. Generate JWT
        String jwtToken = jwtService.generateToken(savedUser);

        // 8. TRIGGER AI (The Fix: This happens BEFORE return)
        // We use the helper method mapProfileToMap to prepare data for Python
        aiTriggerService.triggerPlanGeneration(savedUser, mapProfileToMap(savedProfile));

        // 9. Return Response
        return AuthResponse.builder()
                .token(jwtToken)
                .email(savedUser.getEmail())
                .userId(savedUser.getUserId())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .userId(user.getUserId())
                .build();
    }

    // --- HELPER METHOD TO SEND CLEAN DATA TO PYTHON ---
    private Map<String, Object> mapProfileToMap(UserProfile p) {
        Map<String, Object> map = new HashMap<>();
        // we use .name() or .toString() on Enums to ensure Python gets a String, not a Java Object
        map.put("primaryGoal", p.getPrimaryGoal() != null ? p.getPrimaryGoal().toString() : "MAINTAIN");
        map.put("experienceLevel", p.getExperienceLevel() != null ? p.getExperienceLevel().toString() : "BEGINNER");
        map.put("weightKg", p.getWeightKg());
        map.put("dietaryType", p.getDietaryType() != null ? p.getDietaryType().toString() : "NON_VEG");
        map.put("activityLevel", p.getActivityLevel() != null ? p.getActivityLevel().toString() : "SEDENTARY");
        map.put("workoutLocation", p.getWorkoutLocation() != null ? p.getWorkoutLocation().toString() : "COMMERCIAL_GYM");

        // You can add more fields if your Python prompts need them
        return map;
    }
}