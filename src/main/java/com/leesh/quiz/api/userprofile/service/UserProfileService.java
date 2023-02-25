package com.leesh.quiz.api.userprofile.service;

import com.leesh.quiz.api.quiz.dao.QuizDao;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDto;
import com.leesh.quiz.api.userprofile.dto.answer.EditMyAnswerDto;
import com.leesh.quiz.api.userprofile.dto.answer.UserAnswerDto;
import com.leesh.quiz.api.userprofile.dto.quiz.EditMyQuizDto;
import com.leesh.quiz.api.userprofile.dto.user.UserProfileDto;
import com.leesh.quiz.api.userprofile.dto.user.UsernameDto;
import com.leesh.quiz.domain.answer.Answer;
import com.leesh.quiz.domain.answer.repository.AnswerRepository;
import com.leesh.quiz.domain.answervote.AnswerVote;
import com.leesh.quiz.domain.answervote.AnswerVoteRepository;
import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quiz.repository.QuizRepository;
import com.leesh.quiz.domain.quizvote.QuizVote;
import com.leesh.quiz.domain.quizvote.QuizVoteRepository;
import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import com.leesh.quiz.domain.user.service.UserService;
import com.leesh.quiz.global.constant.PagingRequestInfo;
import com.leesh.quiz.global.constant.PagingResponseDto;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static com.leesh.quiz.global.constant.PagingRequestInfo.from;

@RequiredArgsConstructor
@Transactional
@Service
public class UserProfileService {

    private final QuizDao quizDao;
    private final QuizRepository quizRepository;
    private final AnswerRepository answerRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final QuizVoteRepository quizVoteRepository;
    private final AnswerVoteRepository answerVoteRepository;

    @Transactional(readOnly = true)
    public PagingResponseDto<QuizDto> getUserQuizzesByPaging(Pageable pageable, Long userId) {

        // Pageable, userInfo 를 Dao에서 바인딩 할 수 있도록 PagingRequestInfo로 변환
        // (Mybatis는 pageable 객체의 orders의 getter가 없어서 바인딩 할 수 없기 때문에 변환한다.)
        PagingRequestInfo pagingInfo = from(pageable, userId);

        // Dao에서 페이징 정보를 이용하여, 페이징 처리된 데이터를 가져온다.
        List<QuizDto> content = quizDao.getQuizzesByPaging(pagingInfo);

        // PageableExecutionUtils.getPage()는 count 쿼리에 대해 최적화를 해주는데, 다음과 같을 때 count Query를 생략한다.
        // 1. 첫번째 페이지이면서, 컨텐츠 사이즈가 페이지 사이즈 보다 작을 때
        // 2. 마지막 페이지 일 때, (마지막 페이지이면, 페이즈 사이즈 * (현재 페이지 - 1) + 컨텐츠 사이즈 = 전체 사이즈)
        Page<QuizDto> page = PageableExecutionUtils.getPage(content, pageable,
                () -> quizDao.getTotalCountByUserId(userId));

        // 이 중에서 필요한 정보만 추출하여 반환한다.
        return new PagingResponseDto<>(page);
    }

    public EditMyQuizDto.Response editMyQuiz(EditMyQuizDto.Request request, UserInfo userInfo, Long quizId) {

        // Quiz를 가져온다. 퀴즈의 작성자를 검증하기 위해 User도 함께 가져온다.
        Quiz quiz = findQuizByIdWithUser(quizId);

        // 퀴즈를 수정한다. (실패 시 Business 예외 발생)
        quiz.edit(request.title(), request.contents(), userInfo.userId());

        return EditMyQuizDto.Response.from(quiz.getId());
    }


    public void deleteMyQuiz(Long quizId, UserInfo userInfo) {

        // 먼저, 퀴즈의 작성자 인지 검증을 위해 유저와 함께 fetch join 으로 가져온다.
        // 나머지 연관관계 컬렉션들은 BatchSize를 통해 1+1 쿼리로 가져온다.
        Quiz quiz = findQuizByIdWithUser(quizId);

        // 퀴즈를 삭제한다. (실제 DB에서 삭제가 아닌 논리적으로 삭제)
        quiz.disable(userInfo.userId());

    }

    @Transactional(readOnly = true)
    public PagingResponseDto<UserAnswerDto> getUserAnswers(Pageable pageable, Long userId) {

        Page<UserAnswerDto> page = answerRepository.getUserAnswersByPaging(userId, pageable);

        return new PagingResponseDto<>(page);
    }


    private Quiz findQuizByIdWithUser(Long quizId) {

        return quizRepository.findByQuizIdWithUser(quizId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_QUIZ));
    }

    public EditMyAnswerDto.Response editMyAnswer(EditMyAnswerDto.Request request, UserInfo userInfo, Long answerId) {

        // Answer를 가져온다. 답변의 작성자를 검증하기 위해 User도 함께 가져온다.
        Answer answer = findAnswerByIdWithUser(answerId);

        // 답변을 수정한다. (실패 시 Business 예외 발생)
        answer.edit(request.contents(), userInfo.userId());

        return EditMyAnswerDto.Response.from(answer.getId());

    }

    private Answer findAnswerByIdWithUser(Long answerId) {

        return answerRepository.findByIdWithUser(answerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_ANSWER));
    }


    public void deleteMyAnswer(Long answerId, UserInfo userInfo) {

        // 먼저, 답변의 작성자 인지 검증을 위해 유저와 함께 fetch join 으로 가져온다.
        // 나머지 연관관계 컬렉션들은 BatchSize를 통해 1+1 쿼리로 가져온다.
        Answer answer = findAnswerByIdWithUser(answerId);

        // 자식 엔티티부터 차례대로 삭제한다.
        answer.disable(userInfo.userId());
    }

    public UserProfileDto getUserProfile(Long userId) {

        // 멀티 스레드로 데이터를 Map에 담아서 가져온다.
        Map<String, Object> data = fetchUserDataByParallel(userId);

        User user = (User) data.get("user");
        UserProfileDto.Answers answers = (UserProfileDto.Answers) data.get("answers");
        UserProfileDto.Quizzes quizzes = (UserProfileDto.Quizzes) data.get("quizzes");

        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileImageUrl(user.getProfile())
                .answers(answers)
                .quizzes(quizzes)
                .build();
    }

    private Map<String, Object> fetchUserDataByParallel(Long userId) {

        final Map<String, Object> data = new ConcurrentHashMap<>();

        // CompletableFuture를 이용하여 멀티 스레드로 데이터를 가져온다.
        CompletableFuture<Void> userFuture = CompletableFuture.runAsync(() -> {
            User user = userService.findUserById(userId);
            data.put("user", user);
        });
        CompletableFuture<Void> quizFuture = CompletableFuture.runAsync(() -> {
            UserProfileDto.Quizzes quizzes = quizRepository
                    .getUserQuizCountWithVotesSum(userId)
                    .orElseGet(() -> new UserProfileDto.Quizzes(0, 0));
            data.put("quizzes", quizzes);
        });
        CompletableFuture<Void> answerFuture = CompletableFuture.runAsync(() -> {
            UserProfileDto.Answers answers = answerRepository
                    .getUserAnswerCountWithVotesSum(userId)
                    .orElseGet(() -> new UserProfileDto.Answers(0, 0));
            data.put("answers", answers);
        });

        CompletableFuture
                .allOf(userFuture, quizFuture, answerFuture)
                .exceptionally(e -> {
                    throw new RuntimeException();
                })
                .join();

        return data;
    }

    public UsernameDto.Response editUsername(UsernameDto request, UserInfo userInfo) {

        User user = userRepository.getReferenceById(userInfo.userId());
        user.editUsername(request.username(), userInfo.userId());

        return new UsernameDto.Response(user.getId());
    }

    public void deleteUser(UserInfo userInfo) {

        // User를 가져온다. (없으면 예외 발생)
        User user = userService.findUserById(userInfo.userId());

        // 유저 데이터들을 삭제한다.
        deleteUserDataByParallel(user);

        // 예외없이 모든 유저 데이터들이 삭제되었다면, 유저를 비활성화 한다.
        user.disable(userInfo.userId());

    }

    private void deleteUserDataByParallel(User user) {
        CompletableFuture.allOf(
                        CompletableFuture.runAsync(() -> {
                            // 해당 유저 ID로 된 퀴즈를 모두 삭제한다. (논리적 삭제)
                            List<Long> ids = quizRepository.findQuizzesByUserId(user.getId())
                                    .stream()
                                    .map(Quiz::getId)
                                    .toList();
                            quizRepository.deleteAllInIds(ids);
                        }),
                        CompletableFuture.runAsync(() -> {
                            // 해당 유저 ID로 된 댓글을 모두 삭제한다. (논리적 삭제)
                            List<Long> ids = answerRepository.findAnswersByUserId(user.getId())
                                    .stream()
                                    .map(Answer::getId)
                                    .toList();
                            answerRepository.deleteAllInIds(ids);
                        }),
                        CompletableFuture.runAsync(() -> {
                            // 해당 유저 ID로 된 퀴즈 투표를 모두 삭제한다.
                            List<Long> ids = quizVoteRepository.findQuizVotesByUserId(user.getId())
                                    .stream()
                                    .map(QuizVote::getId)
                                    .toList();
                            quizVoteRepository.deleteAllInIds(ids);
                        }),
                        CompletableFuture.runAsync(() -> {
                            // 해당 유저 ID로 된 댓글 투표를 모두 삭제한다.
                            List<Long> ids = answerVoteRepository.findAnswerVotesByUserId(user.getId())
                                    .stream()
                                    .map(AnswerVote::getId)
                                    .toList();
                            answerVoteRepository.deleteAllInIds(ids);
                        }))
                .exceptionally(e -> {
                    throw new RuntimeException();
                })
                .join();
    }

}
