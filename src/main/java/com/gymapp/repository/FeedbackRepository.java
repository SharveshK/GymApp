package com.gymapp.repository;

import com.gymapp.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    /**
     * Finds all feedback submitted by a specific user,
     * ordered by the most recent first.
     */
    List<Feedback> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

}