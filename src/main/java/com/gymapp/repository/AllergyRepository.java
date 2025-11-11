package com.gymapp.repository;

import com.gymapp.model.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {

    Optional<Allergy> findByName(String name);

    // This is the method for your "search" feature!
    // Spring Data JPA will auto-create the query for "name LIKE '...%'"
    List<Allergy> findByNameStartingWithIgnoreCase(String prefix);
}