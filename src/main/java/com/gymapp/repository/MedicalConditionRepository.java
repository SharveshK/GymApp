package com.gymapp.repository;

import com.gymapp.model.MedicalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalConditionRepository extends JpaRepository<MedicalCondition, Long> {

    Optional<MedicalCondition> findByName(String name);

    // This method powers your "search-as-you-type" feature
    List<MedicalCondition> findByNameStartingWithIgnoreCase(String prefix);
}