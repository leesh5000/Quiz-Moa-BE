package com.leesh.quiz.domain.quiz;

import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;

import java.util.Optional;

public interface QuizDao {

    Optional<QuizDetailDto> getQuizDetail(Long quizId);

}
