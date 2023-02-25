package com.leesh.quiz.domain.quiz.repository;

import com.leesh.quiz.domain.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface QuizRepository extends JpaRepository<Quiz, Long>, QuizDao {

    @Query("select q " +
            "from Quiz q " +
            "join fetch q.user " +
            "where q.id = :id and q.deleted = false")
    Optional<Quiz> findByQuizIdWithUser(@Param("id") Long id);

    @Query("select q " +
            "from Quiz q " +
            "inner join fetch q.user u " +
            "where u.id = :userId and q.deleted = false")
    List<Quiz> findQuizzesByUserId(@Param("userId") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update Quiz q set q.deleted = true where q.id in :ids")
    void deleteAllInIds(@Param("ids") List<Long> ids);
}
