package com.dubai.dlt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(name = "correct_answers", nullable = false)
    private Integer correctAnswers;

    @Column(name = "score_percentage", nullable = false)
    private Double scorePercentage;

    @Column(name = "exam_type", nullable = false)
    private String examType;

    @Column(name = "time_taken_seconds")
    private Integer timeTakenSeconds;

    @Column(name = "passed", nullable = false)
    private Boolean passed;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        scorePercentage = (correctAnswers * 100.0) / totalQuestions;
        passed = scorePercentage >= 80.0;
    }
}
