package com.dubai.dlt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "topic_tag", nullable = false)
    private String topicTag;

    @Column(nullable = false)
    private String difficulty;

    @Column(name = "question_en", columnDefinition = "TEXT", nullable = false)
    private String questionEn;

    @Column(name = "question_ar", columnDefinition = "TEXT", nullable = false)
    private String questionAr;

    @Column(name = "question_hi_ur", columnDefinition = "TEXT", nullable = false)
    private String questionHiUr;

    @Column(name = "a_en", columnDefinition = "TEXT", nullable = false)
    private String aEn;

    @Column(name = "b_en", columnDefinition = "TEXT", nullable = false)
    private String bEn;

    @Column(name = "c_en", columnDefinition = "TEXT", nullable = false)
    private String cEn;

    @Column(name = "d_en", columnDefinition = "TEXT", nullable = false)
    private String dEn;

    @Column(name = "a_ar", columnDefinition = "TEXT", nullable = false)
    private String aAr;

    @Column(name = "b_ar", columnDefinition = "TEXT", nullable = false)
    private String bAr;

    @Column(name = "c_ar", columnDefinition = "TEXT", nullable = false)
    private String cAr;

    @Column(name = "d_ar", columnDefinition = "TEXT", nullable = false)
    private String dAr;

    @Column(name = "a_hi_ur", columnDefinition = "TEXT", nullable = false)
    private String aHiUr;

    @Column(name = "b_hi_ur", columnDefinition = "TEXT", nullable = false)
    private String bHiUr;

    @Column(name = "c_hi_ur", columnDefinition = "TEXT", nullable = false)
    private String cHiUr;

    @Column(name = "d_hi_ur", columnDefinition = "TEXT", nullable = false)
    private String dHiUr;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Column(nullable = false, length = 1)
    private String correct;

    @Column(name = "explanation_en", columnDefinition = "TEXT")
    private String explanationEn;

    @Column(name = "explanation_ar", columnDefinition = "TEXT")
    private String explanationAr;

    @Column(name = "explanation_hi_ur", columnDefinition = "TEXT")
    private String explanationHiUr;
}
