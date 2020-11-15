package com.bloggingplatform.minorproject.dao;

import com.bloggingplatform.minorproject.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.email = :email")
    public User getUserByEmail(@Param("email") String email);
}
