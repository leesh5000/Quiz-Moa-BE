package com.leesh.quiz.domain.quizvote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuizVoteRepository extends JpaRepository<QuizVote, Long> {

    @Query("select qv from QuizVote qv " +
            "inner join fetch qv.user u " +
            "inner join fetch qv.quiz q " +
            "where qv.quiz = :quizId")
    Optional<QuizVote> findQuizDetails(@Param("quizId") Long quizId);
}
