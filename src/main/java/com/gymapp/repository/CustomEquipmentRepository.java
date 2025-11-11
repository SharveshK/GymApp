package com.gymapp.repository;

import com.gymapp.model.CustomEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomEquipmentRepository extends JpaRepository<CustomEquipment, Long> {
    // Find all custom items for a specific profile
    List<CustomEquipment> findByUserProfile_ProfileId(Long profileId);
}