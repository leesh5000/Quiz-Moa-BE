package com.leesh.quiz.domain.answervote;

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
public interface AnswerVoteRepository extends JpaRepository<AnswerVote, Long> {

    @Query(value =
            "select av.id " +
                    "from answer_vote av " +
                    "where av.answer_id = :answerId and av.user_id = :userId",
            nativeQuery = true)
    Optional<Long> findByAnswerIdAndUserId(@Param("answerId") Long answerId,
                                         @Param("userId") Long userId);

    @Query("select av " +
            "from AnswerVote av " +
            "inner join fetch av.user u " +
            "where u.id = :userId")
    List<AnswerVote> findAnswerVotesByUserId(@Param("userId") Long userId);

    // @Query로 벌크 연산 시, @Modifying을 함꼐 사용해야 한다. 그렇지 않으면, InvalidDataAccessApiUsage 예외가 발생한다.
    // Spring Data JPA 스펙
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("delete AnswerVote av where av.id in :ids")
    void deleteAllInIds(@Param("ids") List<Long> ids);
}
