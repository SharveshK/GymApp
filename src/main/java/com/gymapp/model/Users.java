package com.gymapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List; // Make sure to import java.util.List

@Data // Creates getters, setters, toString, equals, hashCode
@Builder // Lets you build objects like: Users.builder().email("...").build()
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password; // We'll store the hash here

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // --- UserDetails Methods ---
    // These are needed for Spring Security.
    // We can hardcode them for now or add a "Role" entity later.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // For now, we'll return a simple authority.
        // Later, you can have a "roles" table and link it here.
        // e.g., return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password; // Returns the password_hash
    }

    @Override
    public String getUsername() {
        return this.email; // We are using email as the username
    }

    // You can set these to true for simplicity in Phase 1
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}