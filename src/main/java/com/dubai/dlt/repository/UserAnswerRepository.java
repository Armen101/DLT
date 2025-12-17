package com.dubai.dlt.repository;

import com.dubai.dlt.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    List<UserAnswer> findByUserIdOrderByAnsweredAtDesc(Long userId);

    List<UserAnswer> findByExamResultId(Long examResultId);

    List<UserAnswer> findByUserIdAndQuestionId(Long userId, Long questionId);
}
