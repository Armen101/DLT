package com.dubai.dlt.service;

import com.dubai.dlt.dto.QuestionDTO;
import com.dubai.dlt.entity.Question;
import com.dubai.dlt.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<QuestionDTO> getQuestionsByTopic(String topic, String language) {
        return questionRepository.findByTopicTag(topic).stream()
                .map(q -> convertToDTO(q, language))
                .collect(Collectors.toList());
    }

    public List<QuestionDTO> getQuestionsByDifficulty(String difficulty, String language) {
        return questionRepository.findByDifficulty(difficulty).stream()
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

        switch (language.toLowerCase()) {
            case "ar":
                dto.setQuestion(question.getQuestionAr());
                dto.setOptionA(question.getAAr());
                dto.setOptionB(question.getBAr());
                dto.setOptionC(question.getCAr());
                dto.setOptionD(question.getDAr());
                dto.setExplanation(question.getExplanationAr());
                break;
            case "hi":
            case "ur":
            case "hi_ur":
                dto.setQuestion(question.getQuestionHiUr());
                dto.setOptionA(question.getAHiUr());
                dto.setOptionB(question.getBHiUr());
                dto.setOptionC(question.getCHiUr());
                dto.setOptionD(question.getDHiUr());
                dto.setExplanation(question.getExplanationHiUr());
                break;
            default:
                dto.setQuestion(question.getQuestionEn());
                dto.setOptionA(question.getAEn());
                dto.setOptionB(question.getBEn());
                dto.setOptionC(question.getCEn());
                dto.setOptionD(question.getDEn());
                dto.setExplanation(question.getExplanationEn());
                break;
        }

        return dto;
    }
}
