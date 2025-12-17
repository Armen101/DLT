package com.dubai.dlt.config;

import com.dubai.dlt.entity.Question;
import com.dubai.dlt.repository.QuestionRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (questionRepository.count() == 0) {
            System.out.println("Loading questions from JSON file...");

            try {
                ObjectMapper mapper = new ObjectMapper();
                InputStream inputStream = new ClassPathResource("questions.json").getInputStream();
                List<QuestionData> questionDataList = mapper.readValue(inputStream, new TypeReference<List<QuestionData>>() {});

                for (QuestionData data : questionDataList) {
                    Question question = new Question();
                    if (data.getId() != null) {
                        question.setId(data.getId());
                    }
                    question.setTopicTag(data.getTopic_tag());
                    question.setDifficulty(data.getDifficulty());
                    question.setQuestionEn(data.getQuestion_en());
                    question.setQuestionAr(data.getQuestion_ar());
                    question.setQuestionHiUr(data.getQuestion_hi_ur());
                    question.setAEn(data.getA_en());
                    question.setBEn(data.getB_en());
                    question.setCEn(data.getC_en());
                    question.setDEn(data.getD_en());
                    question.setAAr(data.getA_ar());
                    question.setBAr(data.getB_ar());
                    question.setCAr(data.getC_ar());
                    question.setDAr(data.getD_ar());
                    question.setAHiUr(data.getA_hi_ur());
                    question.setBHiUr(data.getB_hi_ur());
                    question.setCHiUr(data.getC_hi_ur());
                    question.setDHiUr(data.getD_hi_ur());
                    question.setCorrect(data.getCorrect());
                    question.setExplanationEn(data.getExplanation_en());
                    question.setExplanationAr(data.getExplanation_ar());
                    question.setExplanationHiUr(data.getExplanation_hi_ur());

                    questionRepository.save(question);
                }

                System.out.println("Successfully loaded " + questionDataList.size() + " questions!");
            } catch (Exception e) {
                System.err.println("Error loading questions: " + e.getMessage());
                System.err.println("Please add your questions.json file to src/main/resources/");
            }
        } else {
            System.out.println("Questions already loaded. Skipping data initialization.");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class QuestionData {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("topic_tag")
        private String topic_tag;

        @JsonProperty("difficulty")
        private String difficulty;

        @JsonProperty("question_en")
        private String question_en;

        @JsonProperty("question_ar")
        private String question_ar;

        @JsonProperty("question_hi_ur")
        private String question_hi_ur;

        @JsonProperty("A_en")
        private String A_en;

        @JsonProperty("B_en")
        private String B_en;

        @JsonProperty("C_en")
        private String C_en;

        @JsonProperty("D_en")
        private String D_en;

        @JsonProperty("A_ar")
        private String A_ar;

        @JsonProperty("B_ar")
        private String B_ar;

        @JsonProperty("C_ar")
        private String C_ar;

        @JsonProperty("D_ar")
        private String D_ar;

        @JsonProperty("A_hi_ur")
        private String A_hi_ur;

        @JsonProperty("B_hi_ur")
        private String B_hi_ur;

        @JsonProperty("C_hi_ur")
        private String C_hi_ur;

        @JsonProperty("D_hi_ur")
        private String D_hi_ur;

        @JsonProperty("correct")
        private String correct;

        @JsonProperty("explanation_en")
        private String explanation_en;

        @JsonProperty("explanation_ar")
        private String explanation_ar;

        @JsonProperty("explanation_hi_ur")
        private String explanation_hi_ur;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTopic_tag() { return topic_tag; }
        public void setTopic_tag(String topic_tag) { this.topic_tag = topic_tag; }
        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
        public String getQuestion_en() { return question_en; }
        public void setQuestion_en(String question_en) { this.question_en = question_en; }
        public String getQuestion_ar() { return question_ar; }
        public void setQuestion_ar(String question_ar) { this.question_ar = question_ar; }
        public String getQuestion_hi_ur() { return question_hi_ur; }
        public void setQuestion_hi_ur(String question_hi_ur) { this.question_hi_ur = question_hi_ur; }
        public String getA_en() { return A_en; }
        public void setA_en(String a_en) { A_en = a_en; }
        public String getB_en() { return B_en; }
        public void setB_en(String b_en) { B_en = b_en; }
        public String getC_en() { return C_en; }
        public void setC_en(String c_en) { C_en = c_en; }
        public String getD_en() { return D_en; }
        public void setD_en(String d_en) { D_en = d_en; }
        public String getA_ar() { return A_ar; }
        public void setA_ar(String a_ar) { A_ar = a_ar; }
        public String getB_ar() { return B_ar; }
        public void setB_ar(String b_ar) { B_ar = b_ar; }
        public String getC_ar() { return C_ar; }
        public void setC_ar(String c_ar) { C_ar = c_ar; }
        public String getD_ar() { return D_ar; }
        public void setD_ar(String d_ar) { D_ar = d_ar; }
        public String getA_hi_ur() { return A_hi_ur; }
        public void setA_hi_ur(String a_hi_ur) { A_hi_ur = a_hi_ur; }
        public String getB_hi_ur() { return B_hi_ur; }
        public void setB_hi_ur(String b_hi_ur) { B_hi_ur = b_hi_ur; }
        public String getC_hi_ur() { return C_hi_ur; }
        public void setC_hi_ur(String c_hi_ur) { C_hi_ur = c_hi_ur; }
        public String getD_hi_ur() { return D_hi_ur; }
        public void setD_hi_ur(String d_hi_ur) { D_hi_ur = d_hi_ur; }
        public String getCorrect() { return correct; }
        public void setCorrect(String correct) { this.correct = correct; }
        public String getExplanation_en() { return explanation_en; }
        public void setExplanation_en(String explanation_en) { this.explanation_en = explanation_en; }
        public String getExplanation_ar() { return explanation_ar; }
        public void setExplanation_ar(String explanation_ar) { this.explanation_ar = explanation_ar; }
        public String getExplanation_hi_ur() { return explanation_hi_ur; }
        public void setExplanation_hi_ur(String explanation_hi_ur) { this.explanation_hi_ur = explanation_hi_ur; }
    }
}
