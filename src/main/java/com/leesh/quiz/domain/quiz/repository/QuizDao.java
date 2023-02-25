package com.leesh.quiz.domain.quiz.repository;

import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;
import com.leesh.quiz.api.userprofile.dto.user.UserProfileDto;

import java.util.Optional;

public interface QuizDao {

    Optional<QuizDetailDto> getQuizDetailByQuizId(Long id);

    Optional<UserProfileDto.Quizzes> getUserQuizCountWithVotesSum(Long userId);

}
