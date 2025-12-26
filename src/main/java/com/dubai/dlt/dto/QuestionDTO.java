package com.dubai.dlt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private String topicTag;
    private String difficulty;
    private String question;
    private String image;
    private String correct;
    private String explanation;
    private List<OptionDTO> options;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionDTO {
        private String key;
        private String text;
    }
}
