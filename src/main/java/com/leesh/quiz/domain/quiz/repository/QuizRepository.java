package com.leesh.quiz.domain.quiz.repository;

import com.leesh.quiz.domain.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long>, QuizDao {

    @Query("select q " +
            "from Quiz q " +
            "join fetch q.user " +
            "where q.id = :id and q.deleted = false")
    Optional<Quiz> findByQuizIdWithUser(@Param("id") Long id);

    @Query("select q " +
            "from Quiz q " +
            "join fetch q.user u " +
            "left join fetch q.votes qv " +
            "where u.id = :userId and q.deleted = false")
    List<Quiz> findByUserIdWithVotes(@Param("userId") Long userId);

}
