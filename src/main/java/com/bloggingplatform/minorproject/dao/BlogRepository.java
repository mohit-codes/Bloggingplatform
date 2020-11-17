package com.bloggingplatform.minorproject.dao;

import java.util.List;

import com.bloggingplatform.minorproject.entities.Blog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    // @Query("select * from blog")
    // public List<Blog> findall(){

    // }
}
