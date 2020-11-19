package com.bloggingplatform.minorproject.dao;

import java.util.List;

import com.bloggingplatform.minorproject.entities.Answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    // @Query("select a from Answer a where a.question.questionId = :userid")
    // List<Answer> findByUserId(@Param("questionid") Integer questionid);
}
