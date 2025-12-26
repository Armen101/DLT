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

        // Create list of answer texts to shuffle
        List<String> answerTexts = new ArrayList<>();

        switch (language.toLowerCase()) {
            case "ar":
                dto.setQuestion(question.getQuestionAr());
                answerTexts.add(question.getAAr());
                answerTexts.add(question.getBAr());
                answerTexts.add(question.getCAr());
                answerTexts.add(question.getDAr());
                dto.setExplanation(question.getExplanationAr());
                break;
            case "hi":
            case "ur":
            case "hi_ur":
                dto.setQuestion(question.getQuestionHiUr());
                answerTexts.add(question.getAHiUr());
                answerTexts.add(question.getBHiUr());
                answerTexts.add(question.getCHiUr());
                answerTexts.add(question.getDHiUr());
                dto.setExplanation(question.getExplanationHiUr());
                break;
            default:
                dto.setQuestion(question.getQuestionEn());
                answerTexts.add(question.getAEn());
                answerTexts.add(question.getBEn());
                answerTexts.add(question.getCEn());
                answerTexts.add(question.getDEn());
                dto.setExplanation(question.getExplanationEn());
                break;
        }

        // Find the correct answer text based on the original correct field
        int originalCorrectIndex = question.getCorrect().toUpperCase().charAt(0) - 'A'; // A=0, B=1, C=2, D=3
        String correctAnswerText = answerTexts.get(originalCorrectIndex);

        // Shuffle the answer texts
        Collections.shuffle(answerTexts);

        // Build options with A, B, C, D keys in sequential order
        List<QuestionDTO.OptionDTO> options = new ArrayList<>();
        String newCorrect = "";

        for (int i = 0; i < answerTexts.size(); i++) {
            String key = String.valueOf((char)('A' + i)); // A, B, C, D
            options.add(new QuestionDTO.OptionDTO(key, answerTexts.get(i)));
            // Find where the correct answer ended up
            if (answerTexts.get(i).equals(correctAnswerText)) {
                newCorrect = key;
            }
        }

        dto.setOptions(options);
        dto.setImage(question.getImage());
        dto.setCorrect(newCorrect);

        return dto;
    }
}
