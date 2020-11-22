package com.bloggingplatform.minorproject.dao;

import com.bloggingplatform.minorproject.entities.Answer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    // @Query("select a from Answer a where a.question.questionId = :userid")
    // List<Answer> findByUserId(@Param("questionid") Integer questionid);
}
