package com.leesh.quiz.domain.quizvote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuizVoteRepository extends JpaRepository<QuizVote, Long> {

    // 해당 퀴즈에 투표한 사용자인지 확인만 하는 간단한 쿼리이므로,
    // 굳이 Fetch Join을 사용하여 무거운 데이터들을 가져오기 보단, 간단한 native Query로 작성한다.
    @Query(value =
            "select qv.id " +
            "from quiz_vote qv " +
            "where qv.quiz_id = :quizId and qv.user_id = :userId",
            nativeQuery = true)
    Optional<Long> findByQuizIdAndUserId(@Param("quizId") Long quizId,
                                         @Param("userId") Long userId);
}
