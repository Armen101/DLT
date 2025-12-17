package com.dubai.dlt.repository;

import com.dubai.dlt.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByTopicTag(String topicTag);

    List<Question> findByDifficulty(String difficulty);

    List<Question> findByTopicTagAndDifficulty(String topicTag, String difficulty);

    @Query(value = "SELECT * FROM questions ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Question> findRandomQuestions(@Param("count") int count);

    @Query(value = "SELECT * FROM questions WHERE topic_tag = :topic ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Question> findRandomQuestionsByTopic(@Param("topic") String topic, @Param("count") int count);

    @Query("SELECT DISTINCT q.topicTag FROM Question q")
    List<String> findAllTopics();
}
