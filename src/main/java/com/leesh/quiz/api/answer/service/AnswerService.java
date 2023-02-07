package com.leesh.quiz.api.answer.service;

import com.leesh.quiz.api.answer.dto.CreateAnswerVoteDto;
import com.leesh.quiz.domain.answer.Answer;
import com.leesh.quiz.domain.answer.repository.AnswerRepository;
import com.leesh.quiz.domain.answervote.AnswerVote;
import com.leesh.quiz.domain.answervote.AnswerVoteRepository;
import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AnswerService {

    private final AnswerVoteRepository answerVoteRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public CreateAnswerVoteDto.Response createAnswerVote(UserInfo userInfo, Long answerId, CreateAnswerVoteDto.Request request) {

        // 먼저, 해당 유저가 이미 투표를 했는지 확인한다.
        answerVoteRepository.findByAnswerIdAndUserId(answerId, userInfo.userId())
                .ifPresent(v -> {
                    throw new BusinessException(ErrorCode.ALREADY_EXIST_VOTER);
                });

        // 퀴즈나 답변 엔티티의 생성/수정/삭제는 매우 드물게 일어난다.
        // 예를들어, 애플리케이션은 대부분 10번 조회가 일어나는 동안 1번의 생성/수정/삭제가 일어난다고 한다.
        // 또한, 퀴즈나 답변 엔티티는 매우 중요한 데이터이므로 정합성이 매우 중요하다.
        // 이러한 이유로 퀴즈나 답변 엔티티의 생성/수정/삭제는 직접 데이터를 조회하여 정합성을 확인한다.
        // 하지만, 투표 데이터의 경우, 유저들은 글을 쓰는 것 보다 글을 읽고 투표하는 행위는 매우 자주한다.
        // 또한, 투표 데이터의 경우 그렇게 정합성이 중요한 데이터가 아니다.
        // 이러한 이유로, 투표 데이터의 경우 DB를 직접 조회하지 않고, 프록시로 조회하여 최적화한다.
        Answer answer = answerRepository.getReferenceById(answerId);
        User user = userRepository.getReferenceById(userInfo.userId());

        AnswerVote answerVote = answerVoteRepository.save(
                request.toEntity(user, answer));

        return CreateAnswerVoteDto.Response.from(answerVote.getId());
    }
}
