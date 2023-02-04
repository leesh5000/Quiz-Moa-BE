package com.leesh.quiz.api.userprofile.service;

import com.leesh.quiz.api.userprofile.dao.PagingRequestInfo;
import com.leesh.quiz.api.userprofile.dao.UserProfileDao;
import com.leesh.quiz.api.userprofile.dto.DeleteMyQuizDto;
import com.leesh.quiz.api.userprofile.dto.EditMyQuizDto;
import com.leesh.quiz.api.userprofile.dto.MyQuizDto;
import com.leesh.quiz.api.userprofile.dto.PagingResponseDto;
import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quiz.QuizRepository;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.leesh.quiz.api.userprofile.dao.PagingRequestInfo.from;

@RequiredArgsConstructor
@Transactional
@Service
public class UserProfileService {

    private final UserProfileDao userProfileDao;
    private final QuizRepository quizRepository;

    @Transactional(readOnly = true)
    public PagingResponseDto getMyQuizzes(Pageable pageable, UserInfo userInfo) {

        // Pageable, userInfo 를 Dao에서 바인딩 할 수 있도록 PagingRequestInfo로 변환
        PagingRequestInfo pagingInfo = from(pageable, userInfo);

        // Dao에서 페이징 정보를 이용하여, 페이징 처리된 데이터를 가져온다.
        List<MyQuizDto> myQuizzes = userProfileDao.getQuizzesByPaging(pagingInfo);
        Long totalCount = userProfileDao.getTotalCount(userInfo.userId());

        // Spring Data JPA의 Page를 구현함으로써, last, first, totalElements 등의 정보를 자동으로 계산해준다.
        Page<MyQuizDto> page = new PageImpl<>(myQuizzes, pageable, totalCount);

        // 이 중에서 필요한 정보만 추출하여 반환한다.
        return PagingResponseDto.from(page);
    }

    public EditMyQuizDto.Response editMyQuiz(EditMyQuizDto.Request request, UserInfo userInfo, Long quizId) {

        // Quiz를 가져온다. 퀴즈의 작성자를 검증하기 위해 User도 함께 가져온다.
        Quiz quiz = findQuizByIdWithUser(quizId);

        // 퀴즈를 수정한다. (실패 시 Business 예외 발생)
        quiz.edit(request.title(), request.contents(), userInfo.userId());

        return EditMyQuizDto.Response.from(quiz.getId());
    }


    public DeleteMyQuizDto deleteMyQuiz(Long quizId, UserInfo userInfo) {

        // 먼저, 퀴즈의 작성자 인지 검증을 위해 유저와 함께 fetch join 으로 가져온다.
        // 나머지 연관관계 컬렉션들은 BatchSize를 통해 1+1 쿼리로 가져온다.
        Quiz quiz = findQuizByIdWithUser(quizId);

        // 자식 엔티티부터 차례대로 삭제한다.
        quiz.delete(userInfo.userId());

        return DeleteMyQuizDto.from(quizId);
    }

    private Quiz findQuizByIdWithUser(Long quizId) {

        return quizRepository.findByIdWithUser(quizId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_QUIZ));
    }

}
