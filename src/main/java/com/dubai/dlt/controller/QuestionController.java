package com.dubai.dlt.controller;

import com.dubai.dlt.dto.QuestionDTO;
import com.dubai.dlt.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(
            @RequestParam(defaultValue = "en") String language) {
        return ResponseEntity.ok(questionService.getAllQuestions(language));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String language) {
        return ResponseEntity.ok(questionService.getQuestionById(id, language));
    }

    @GetMapping("/topic/{topic}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByTopic(
            @PathVariable String topic,
            @RequestParam(defaultValue = "20") int count,
            @RequestParam(defaultValue = "en") String language) {
        return ResponseEntity.ok(questionService.getQuestionsByTopic(topic, count, language));
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByDifficulty(
            @PathVariable String difficulty,
            @RequestParam(defaultValue = "20") int count,
            @RequestParam(defaultValue = "en") String language) {
        return ResponseEntity.ok(questionService.getQuestionsByDifficulty(difficulty, count, language));
    }

    @GetMapping("/random")
    public ResponseEntity<List<QuestionDTO>> getRandomQuestions(
            @RequestParam(defaultValue = "20") int count,
            @RequestParam(defaultValue = "en") String language) {
        return ResponseEntity.ok(questionService.getRandomQuestions(count, language));
    }

    @GetMapping("/random/topic/{topic}")
    public ResponseEntity<List<QuestionDTO>> getRandomQuestionsByTopic(
            @PathVariable String topic,
            @RequestParam(defaultValue = "20") int count,
            @RequestParam(defaultValue = "en") String language) {
        return ResponseEntity.ok(questionService.getRandomQuestionsByTopic(topic, count, language));
    }

    @GetMapping("/topics")
    public ResponseEntity<List<String>> getAllTopics() {
        return ResponseEntity.ok(questionService.getAllTopics());
    }
}
