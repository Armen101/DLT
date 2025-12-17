package com.dubai.dlt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsDTO {
    private Long userId;
    private Long totalExams;
    private Long passedExams;
    private Double averageScore;
    private Long totalQuestionsAnswered;
}
