package com.dubai.dlt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmissionDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Exam type is required")
    private String examType;

    private Integer timeTakenSeconds;

    @NotNull(message = "Answers are required")
    private List<AnswerDTO> answers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDTO {
        @NotNull(message = "Question ID is required")
        private Long questionId;

        @NotNull(message = "Selected answer is required")
        private String selectedAnswer;
    }
}
