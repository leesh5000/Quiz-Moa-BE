package com.leesh.quiz.domain.quiz.repository;

import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;

import java.util.Optional;

public interface QuizDao {

    Optional<QuizDetailDto> getQuizDetailByQuizId(Long id);

}
