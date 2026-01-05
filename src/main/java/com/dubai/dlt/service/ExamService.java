package com.dubai.dlt.service;

import com.dubai.dlt.dto.ExamResultDTO;
import com.dubai.dlt.dto.ExamSubmissionDTO;
import com.dubai.dlt.dto.TopicPerformanceDTO;
import com.dubai.dlt.dto.UserStatsDTO;
import com.dubai.dlt.entity.ExamResult;
import com.dubai.dlt.entity.Question;
import com.dubai.dlt.entity.User;
import com.dubai.dlt.entity.UserAnswer;
import com.dubai.dlt.repository.ExamResultRepository;
import com.dubai.dlt.repository.QuestionRepository;
import com.dubai.dlt.repository.UserAnswerRepository;
import com.dubai.dlt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Transactional
    public ExamResultDTO submitExam(ExamSubmissionDTO submission) {
        User user = userRepository.findById(submission.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + submission.getUserId()));

        ExamResult examResult = new ExamResult();
        examResult.setUser(user);
        examResult.setTotalQuestions(submission.getAnswers().size());
        examResult.setExamType(submission.getExamType());
        examResult.setTimeTakenSeconds(submission.getTimeTakenSeconds());

        int correctCount = 0;
        List<ExamResultDTO.QuestionResultDTO> questionResults = new ArrayList<>();

        for (ExamSubmissionDTO.AnswerDTO answer : submission.getAnswers()) {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found with id: " + answer.getQuestionId()));

            boolean isCorrect = question.getCorrect().equalsIgnoreCase(answer.getSelectedAnswer());
            if (isCorrect) {
                correctCount++;
            }

            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setUser(user);
            userAnswer.setQuestion(question);
            userAnswer.setSelectedAnswer(answer.getSelectedAnswer());
            userAnswer.setIsCorrect(isCorrect);
            userAnswer.setExamResult(examResult);
            userAnswerRepository.save(userAnswer);

            ExamResultDTO.QuestionResultDTO questionResult = new ExamResultDTO.QuestionResultDTO();
            questionResult.setQuestionId(question.getId());
            questionResult.setSelectedAnswer(answer.getSelectedAnswer());
            questionResult.setCorrectAnswer(question.getCorrect());
            questionResult.setIsCorrect(isCorrect);
            questionResults.add(questionResult);
        }

        examResult.setCorrectAnswers(correctCount);
        ExamResult savedResult = examResultRepository.save(examResult);

        return convertToDTO(savedResult, questionResults);
    }

    public List<ExamResultDTO> getUserExamHistory(Long userId) {
        return examResultRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(result -> convertToDTO(result, null))
                .collect(Collectors.toList());
    }

    public ExamResultDTO getExamResultById(Long resultId) {
        ExamResult result = examResultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Exam result not found with id: " + resultId));

        List<UserAnswer> answers = userAnswerRepository.findByExamResultId(resultId);
        List<ExamResultDTO.QuestionResultDTO> questionResults = answers.stream()
                .map(answer -> {
                    ExamResultDTO.QuestionResultDTO qr = new ExamResultDTO.QuestionResultDTO();
                    qr.setQuestionId(answer.getQuestion().getId());
                    qr.setSelectedAnswer(answer.getSelectedAnswer());
                    qr.setCorrectAnswer(answer.getQuestion().getCorrect());
                    qr.setIsCorrect(answer.getIsCorrect());
                    return qr;
                })
                .collect(Collectors.toList());

        return convertToDTO(result, questionResults);
    }

    public UserStatsDTO getUserStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Long totalExams = examResultRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().count();
        Long passedExams = examResultRepository.countPassedExamsByUserId(userId);
        Double averageScore = examResultRepository.findAverageScoreByUserId(userId);
        Long totalQuestionsAnswered = userAnswerRepository.findByUserIdOrderByAnsweredAtDesc(userId).stream().count();

        UserStatsDTO stats = new UserStatsDTO();
        stats.setUserId(userId);
        stats.setTotalExams(totalExams);
        stats.setPassedExams(passedExams);
        stats.setAverageScore(averageScore != null ? averageScore : 0.0);
        stats.setTotalQuestionsAnswered(totalQuestionsAnswered);

        return stats;
    }

    public List<TopicPerformanceDTO> getTopicPerformance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<Object[]> results = userAnswerRepository.findTopicPerformanceByUserId(userId);

        return results.stream()
                .map(row -> {
                    String topic = (String) row[0];
                    Long totalQuestions = ((Number) row[1]).longValue();
                    Long correctAnswers = ((Number) row[2]).longValue();
                    Double percentage = (correctAnswers.doubleValue() / totalQuestions.doubleValue()) * 100;

                    return new TopicPerformanceDTO(topic, totalQuestions, correctAnswers, percentage);
                })
                .collect(Collectors.toList());
    }

    private ExamResultDTO convertToDTO(ExamResult result, List<ExamResultDTO.QuestionResultDTO> questionResults) {
        ExamResultDTO dto = new ExamResultDTO();
        dto.setId(result.getId());
        dto.setUserId(result.getUser().getId());
        dto.setTotalQuestions(result.getTotalQuestions());
        dto.setCorrectAnswers(result.getCorrectAnswers());
        dto.setScorePercentage(result.getScorePercentage());
        dto.setExamType(result.getExamType());
        dto.setTimeTakenSeconds(result.getTimeTakenSeconds());
        dto.setPassed(result.getPassed());
        dto.setCreatedAt(result.getCreatedAt());
        dto.setQuestionResults(questionResults);
        return dto;
    }
}
