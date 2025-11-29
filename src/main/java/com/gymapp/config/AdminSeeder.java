package com.gymapp.config;

import com.gymapp.model.Users;
import com.gymapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public AdminSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // ⚠️ REPLACE THIS WITH THE EXACT EMAIL FROM YOUR .ENV FILE ⚠️
        String aiEmail = "karthiksharvesh6@gmail.com";

        Users aiUser = userRepository.findByEmail(aiEmail).orElse(null);

        if (aiUser != null) {
            // We use "ROLE_ADMIN" because Spring Security likes the "ROLE_" prefix
            if (!"ROLE_ADMIN".equals(aiUser.getRole())) {
                aiUser.setRole("ROLE_ADMIN");
                userRepository.save(aiUser);
                System.out.println("✅ ADMIN SEEDER: Success! " + aiEmail + " is now an ADMIN.");
            }
        } else {
            System.out.println("⚠️ Admin Seeder: AI User (" + aiEmail + ") not found. Please Register it or check the email string.");
        }
    }
}