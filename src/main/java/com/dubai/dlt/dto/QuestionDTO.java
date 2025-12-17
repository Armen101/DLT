package com.dubai.dlt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private String topicTag;
    private String difficulty;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String explanation;
}
