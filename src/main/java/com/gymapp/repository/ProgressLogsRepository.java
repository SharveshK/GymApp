package com.gymapp.repository;

import com.gymapp.model.ProgressLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressLogsRepository extends JpaRepository<ProgressLogs, Long> {

    // --- FIX 1 ---
    // Renamed from findByUserId... to findByUser_UserId...
    List<ProgressLogs> findByUser_UserIdOrderByLogDateAsc(Long userId);

    // --- FIX 2 ---
    // Renamed from findByUserIdAndLogDate to findByUser_UserIdAndLogDate
    Optional<ProgressLogs> findByUser_UserIdAndLogDate(Long userId, LocalDate logDate);
}