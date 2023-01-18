package com.leesh.quiz.service;

import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quiz.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyQuizService {

    private final QuizRepository quizRepository;

    public void findMyQuizzes(String userNickname, Pageable pageable) {

        Page<Quiz> all = quizRepository.findAll(userNickname, pageable);

        System.out.printf("");


    }
}
