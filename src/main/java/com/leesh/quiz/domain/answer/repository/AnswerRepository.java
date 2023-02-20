package com.leesh.quiz.domain.answer.repository;

import com.leesh.quiz.domain.answer.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerDao {

    @Query("select a " +
            "from Answer a " +
            "join fetch a.user " +
            "where a.id = :id and a.deleted = false")
    Optional<Answer> findByIdWithUser(@Param("id") Long id);

    @Query("select a " +
            "from Answer a " +
            "join fetch a.user u " +
            "left join fetch a.votes av " +
            "where u.id = :userId and a.deleted = false")
    List<Answer> findByUserIdWithVotes(@Param("userId") Long profileUserId);

}
