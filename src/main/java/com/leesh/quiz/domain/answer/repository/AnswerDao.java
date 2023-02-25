package com.leesh.quiz.domain.answer.repository;

import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;
import com.leesh.quiz.api.userprofile.dto.answer.UserAnswerDto;
import com.leesh.quiz.api.userprofile.dto.user.UserProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AnswerDao {

    Page<UserAnswerDto> getUserAnswersByPaging(Long userId, Pageable pageable);

    Optional<List<QuizDetailDto.AnswerDto>> getAnswersWithVoteByQuizId(Long quizId);

    Optional<UserProfileDto.Answers> getUserAnswerCountWithVotesSum(Long userId);
}
