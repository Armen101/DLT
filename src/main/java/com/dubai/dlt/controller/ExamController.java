package com.dubai.dlt.controller;

import com.dubai.dlt.dto.ExamResultDTO;
import com.dubai.dlt.dto.ExamSubmissionDTO;
import com.dubai.dlt.dto.TopicPerformanceDTO;
import com.dubai.dlt.dto.UserStatsDTO;
import com.dubai.dlt.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping("/submit")
    public ResponseEntity<ExamResultDTO> submitExam(@Valid @RequestBody ExamSubmissionDTO submission) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examService.submitExam(submission));
    }

    @GetMapping("/user/{userId}/history")
    public ResponseEntity<List<ExamResultDTO>> getUserExamHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(examService.getUserExamHistory(userId));
    }

    @GetMapping("/result/{resultId}")
    public ResponseEntity<ExamResultDTO> getExamResultById(@PathVariable Long resultId) {
        return ResponseEntity.ok(examService.getExamResultById(resultId));
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<UserStatsDTO> getUserStats(@PathVariable Long userId) {
        return ResponseEntity.ok(examService.getUserStats(userId));
    }

    @GetMapping("/user/{userId}/performance-by-topic")
    public ResponseEntity<List<TopicPerformanceDTO>> getTopicPerformance(@PathVariable Long userId) {
        return ResponseEntity.ok(examService.getTopicPerformance(userId));
    }
}
