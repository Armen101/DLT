package com.dubai.dlt.service;

import com.dubai.dlt.dto.QuestionDTO;
import com.dubai.dlt.entity.Question;
import com.dubai.dlt.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<QuestionDTO> getAllQuestions(String language) {
        return questionRepository.findAll().stream()
                .map(q -> convertToDTO(q, language))
                .collect(Collectors.toList());
    }

    public QuestionDTO getQuestionById(Long id, String language) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        return convertToDTO(question, language);
    }

    public List<QuestionDTO> getQuestionsByTopic(String topic, int count, String language) {
        return questionRepository.findByTopicTag(topic).stream()
                .limit(count)
                .map(q -> convertToDTO(q, language))
                .collect(Collectors.toList());
    }

    public List<QuestionDTO> getQuestionsByDifficulty(String difficulty, int count, String language) {
        return questionRepository.findByDifficulty(difficulty).stream()
                .limit(count)
                .map(q -> convertToDTO(q, language))
                .collect(Collectors.toList());
    }

    public List<QuestionDTO> getRandomQuestions(int count, String language) {
        return questionRepository.findRandomQuestions(count).stream()
                .map(q -> convertToDTO(q, language))
                .collect(Collectors.toList());
    }

    public List<QuestionDTO> getRandomQuestionsByTopic(String topic, int count, String language) {
        return questionRepository.findRandomQuestionsByTopic(topic, count).stream()
                .map(q -> convertToDTO(q, language))
                .collect(Collectors.toList());
    }

    public List<String> getAllTopics() {
        return questionRepository.findAllTopics();
    }

    private QuestionDTO convertToDTO(Question question, String language) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setTopicTag(question.getTopicTag());
        dto.setDifficulty(question.getDifficulty());

        // Create list of answer texts in original order (no shuffling for MVP)
        List<String> answerTexts = new ArrayList<>();

        switch (language.toLowerCase()) {
            case "ar":
                dto.setQuestion(question.getQuestionAr());
                answerTexts.add(question.getAAr());
                answerTexts.add(question.getBAr());
                answerTexts.add(question.getCAr());
                dto.setExplanation(question.getExplanationAr());
                break;
            case "hi":
            case "ur":
            case "hi_ur":
                dto.setQuestion(question.getQuestionHiUr());
                answerTexts.add(question.getAHiUr());
                answerTexts.add(question.getBHiUr());
                answerTexts.add(question.getCHiUr());
                dto.setExplanation(question.getExplanationHiUr());
                break;
            default:
                dto.setQuestion(question.getQuestionEn());
                answerTexts.add(question.getAEn());
                answerTexts.add(question.getBEn());
                answerTexts.add(question.getCEn());
                dto.setExplanation(question.getExplanationEn());
                break;
        }

        // Build options with A, B, C keys in original order
        List<QuestionDTO.OptionDTO> options = new ArrayList<>();

        for (int i = 0; i < answerTexts.size(); i++) {
            String key = String.valueOf((char)('A' + i)); // A, B, C
            options.add(new QuestionDTO.OptionDTO(key, answerTexts.get(i)));
        }

        dto.setOptions(options);
        dto.setImage(question.getImage());
        dto.setCorrect(question.getCorrect());

        return dto;
    }
}
