package com.dubai.dlt.repository;

import com.dubai.dlt.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    List<UserAnswer> findByUserIdOrderByAnsweredAtDesc(Long userId);

    List<UserAnswer> findByExamResultId(Long examResultId);

    List<UserAnswer> findByUserIdAndQuestionId(Long userId, Long questionId);

    @Query("SELECT q.topicTag as topic, " +
           "COUNT(ua) as totalQuestions, " +
           "SUM(CASE WHEN ua.isCorrect = true THEN 1 ELSE 0 END) as correctAnswers " +
           "FROM UserAnswer ua " +
           "JOIN ua.question q " +
           "WHERE ua.user.id = :userId " +
           "GROUP BY q.topicTag")
    List<Object[]> findTopicPerformanceByUserId(@Param("userId") Long userId);
}
