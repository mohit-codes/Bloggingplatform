package com.bloggingplatform.minorproject.dao;

import java.util.List;

import com.bloggingplatform.minorproject.entities.Blog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    @Query("select b from Blog b where b.user.id = :userid")
    public List<Blog> findByEmail(@Param("userid") Integer userid);

}
