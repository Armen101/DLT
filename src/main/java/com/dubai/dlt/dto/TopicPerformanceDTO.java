package com.dubai.dlt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicPerformanceDTO {
    private String topic;
    private Long totalQuestions;
    private Long correctAnswers;
    private Double percentage;
}
