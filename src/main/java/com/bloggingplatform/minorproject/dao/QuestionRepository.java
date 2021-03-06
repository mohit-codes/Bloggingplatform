package com.bloggingplatform.minorproject.dao;

import java.util.List;

import com.bloggingplatform.minorproject.entities.Question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query("select q from Question q where q.questionId = :id")
    Question findQuestionById(@Param("id") Integer id);

    @Query("select q from Question q where q.user.id <> :userid")
    Page<Question> findByUserIdNotAsked(@Param("userid") Integer userid, Pageable pePageable);

    @Query("select q from Question q where q.user.id = :userid")
    Page<Question> findByUserId(@Param("userid") Integer userid, Pageable pePageable);

    @Query("select q from Question q where q.topic = :topic")
    public List<Question> findAllByTopic(@Param("topic") String topic);
}
