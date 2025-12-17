package com.dubai.dlt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultDTO {
    private Long id;
    private Long userId;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Double scorePercentage;
    private String examType;
    private Integer timeTakenSeconds;
    private Boolean passed;
    private LocalDateTime createdAt;
    private List<QuestionResultDTO> questionResults;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionResultDTO {
        private Long questionId;
        private String selectedAnswer;
        private String correctAnswer;
        private Boolean isCorrect;
    }
}
