package com.leesh.quiz.api.quiz.service;

import com.leesh.quiz.api.quiz.dao.QuizDao;
import com.leesh.quiz.api.quiz.dto.answer.CreateAnswerDto;
import com.leesh.quiz.api.quiz.dto.quiz.CreateQuizDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDto;
import com.leesh.quiz.api.quiz.dto.vote.QuizVoteDto;
import com.leesh.quiz.domain.answer.Answer;
import com.leesh.quiz.domain.answer.repository.AnswerRepository;
import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quiz.repository.QuizRepository;
import com.leesh.quiz.domain.quizvote.QuizVoteRepository;
import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.global.constant.PagingRequestInfo;
import com.leesh.quiz.global.constant.PagingResponseDto;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.BusinessException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizDao quizDao;
    private final AnswerRepository answerRepository;
    private final QuizVoteRepository quizVoteRepository;

    public CreateQuizDto.Response createQuiz(UserInfo userInfo, CreateQuizDto.Request request) {

        // User userProxy = userRepository.getReferenceById(userInfo.userId());

        // Quiz Insert를 위해, User 한 번 더 조회해야하는가?
        // 이 문제는 getReferenceById를 사용하여 Quiz에 프록시를 넣어줌으로써 select 쿼리 한 번을 아낄 수 있지만,
        // 사실 PK를 찍어서 select 쿼리를 날리는 것은 전체 애플리케이션에 비하면 아주 미미한 비용이다.
        // 차라리, findById로 진짜 User를 조회함으로써 데이터 정합성을 보장하는 것이 더 좋은 방법이다.
        // (프록시로 조회하면, 해당 유저가 진짜 존재하는지 알 수 없기 때문이다. 물론 DB에 외래키가 걸려있다면, 예외가 올라오겠지만
        // 외래키가 걸려있지 않다면 데이터 정합성을 해치게 된다. 즉, 작성자가 없는 퀴즈가 생성되는 것이다.)

        User user = userRepository.findById(userInfo.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_USER));

        Quiz quiz = request.toEntity(user);

        return CreateQuizDto.Response.from(
                quizRepository.save(quiz).getId());
    }

    @Transactional(readOnly = true)
    public PagingResponseDto<QuizDto> getQuizzesByPaging(Pageable pageable) {

        List<QuizDto> content = quizDao.getQuizzesByPaging(
                PagingRequestInfo.from(pageable));

        Page<QuizDto> page = PageableExecutionUtils.getPage(content, pageable, quizDao::getTotalCount);

        return new PagingResponseDto<>(page);
    }

    @Transactional(readOnly = true)
    public QuizDetailDto getQuizDetail(@NotNull Long quizId) {

        // 한 방 쿼리로, 퀴즈, 퀴즈 작성자, 퀴즈-투표, 퀴즈-투표 작성자, 답변, 답변 작성자, 답변-투표, 답변-투표 작성자를 조회하는 것은
        // Row 수가 N 관계에 있는 테이블 만큼 늘어나게 되므로 나눠서 조회한다.
        // 어쨌든 N 관계에 있는 모든 Row가 애플리케이션 메모리까지 올라오니까 DB에서 최대한 줄여서 가져와야 한다.

        // 우선, quiz-id로 퀴즈, 퀴즈 작성자, 퀴즈-투표, 퀴즈-투표 작성자 정보를 조회한다.
        QuizDetailDto quizDetail = quizRepository.getQuizDetailByQuizId(quizId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_QUIZ));

        // 해당 quiz에 대한 answer를 조회한 후, 값이 존재하면 퀴즈에 추가한다.
        answerRepository.getAnswersWithVoteByQuizId(quizId)
                .ifPresent(a -> quizDetail.answers().addAll(a));

        return quizDetail;

    }

    public CreateAnswerDto.Response createAnswer(UserInfo userInfo, Long quizId, CreateAnswerDto.Request request) {

        // JPA의 getReferenceById를 사용하면, 조회 쿼리를 하지 않고 Answer 엔티티를 저장할 수 있지만,
        // 데이터의 정합성을 위해 findById로 직접 조회한다. (PK 기반의 Select는 전체 애플리케이션에 비하면 아주 미비한 비용이므로)

        // 해당 답변의 작성자가 유효한지 찾는다.
        User user = userRepository.findById(userInfo.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_USER));

        // 해당 퀴즈가 유효한지 찾는다.
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_QUIZ));

        Answer answer = request.toEntity(user, quiz);

        return CreateAnswerDto.Response.from(
                answerRepository.save(answer).getId());
    }


    public void vote(UserInfo userInfo, Long quizId, QuizVoteDto.Request request) {

    }
}
