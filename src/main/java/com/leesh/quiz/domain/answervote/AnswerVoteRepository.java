package com.leesh.quiz.domain.answervote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerVoteRepository extends JpaRepository<AnswerVote, Long> {

    @Query(value =
            "select av.id " +
                    "from answer_vote av " +
                    "where av.answer_id = :answerId and av.user_id = :userId",
            nativeQuery = true)
    Optional<Long> findByAnswerIdAndUserId(@Param("answerId") Long answerId,
                                         @Param("userId") Long userId);

}
