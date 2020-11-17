package com.bloggingplatform.minorproject.dao;

import java.util.List;

import com.bloggingplatform.minorproject.entities.Question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query("select q from Question q where q.user.id <> :userid")
    public List<Question> findByUserIdNotAsked(@Param("userid") Integer userid);

    @Query("select q from Question q where q.user.id = :userid")
    public List<Question> findByUserId(@Param("userid") Integer userid);
}
