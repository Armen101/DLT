package com.dubai.dlt.repository;

import com.dubai.dlt.entity.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {

    List<ExamResult> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<ExamResult> findByUserIdAndExamType(Long userId, String examType);

    @Query("SELECT AVG(e.scorePercentage) FROM ExamResult e WHERE e.user.id = :userId")
    Double findAverageScoreByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(e) FROM ExamResult e WHERE e.user.id = :userId AND e.passed = true")
    Long countPassedExamsByUserId(@Param("userId") Long userId);
}
