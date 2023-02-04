package com.leesh.quiz.domain.quiz;

import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class QuizService {

    // 도메인 레포지토리는 오로지 도메인 서비스 레이어에서만 의존하도록 한다.
    private final QuizRepository quizRepository;

    public Quiz findByIdWithUser(Long id) {

        return quizRepository.findByIdWithUser(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_QUIZ));

    }
}
