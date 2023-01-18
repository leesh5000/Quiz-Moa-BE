package com.leesh.quiz.domain.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("select q from Quiz q")
    Page<Quiz> findAll(@Param("nickname") String userNickname, Pageable pageable);
}
