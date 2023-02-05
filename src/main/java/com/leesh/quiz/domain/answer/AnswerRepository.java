package com.leesh.quiz.domain.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerDao {

    @Query("select a from Answer a join fetch a.user where a.id = :id and a.deleted = false")
    Optional<Answer> findByIdWithUser(@Param("id") Long id);
}
