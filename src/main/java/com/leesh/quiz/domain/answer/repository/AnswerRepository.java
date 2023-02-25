package com.leesh.quiz.domain.answer.repository;

import com.leesh.quiz.domain.answer.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerDao {

    @Query("select a " +
            "from Answer a " +
            "join fetch a.user " +
            "where a.id = :id and a.deleted = false")
    Optional<Answer> findByIdWithUser(@Param("id") Long id);

    @Query("select a " +
            "from Answer a " +
            "inner join fetch a.user u " +
            "where u.id = :userId and a.deleted = false")
    List<Answer> findAnswersByUserId(@Param("userId") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update Answer a set a.deleted = true where a.id in :ids")
    void deleteAllInIds(@Param("ids") List<Long> ids);
}
