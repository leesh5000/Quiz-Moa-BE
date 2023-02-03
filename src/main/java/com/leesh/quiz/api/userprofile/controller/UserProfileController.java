package com.leesh.quiz.api.userprofile.controller;

import com.leesh.quiz.api.userprofile.dao.UserProfileDao;
import com.leesh.quiz.api.userprofile.dto.MyQuizDto;
import com.leesh.quiz.api.userprofile.dto.PagingResponseDto;
import com.leesh.quiz.domain.quiz.QuizRepository;
import com.leesh.quiz.global.constant.UserInfo;
import com.leesh.quiz.global.validator.UserInfoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.leesh.quiz.api.userprofile.dao.UserProfilePagingInfo.from;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{user-id}")
public class UserProfileController {

    private final UserProfileDao userProfileDao;
    private final QuizRepository quizRepository;

    // Paging은 Spring Data Jpa에서 제공하는 Pageable 스펙을 사용
    // 다음과 같은 형식 /quizzes?page={page}&size={size}&sort={property,direction}
    @GetMapping("/quizzes")
    public ResponseEntity<PagingResponseDto> getMyQuizzes(@PathVariable("user-id") Long userId,
                                                        @AuthenticationPrincipal UserInfo userInfo,
                                                        @PageableDefault(size = 20) Pageable pageable) {

        // 접근 권한이 있는 사용자인지 검증
        UserInfoValidator.isAccessible(userId, userInfo);

        // 화면을 위한 DB 조회로, 별다른 로직이 필요하지 않아 Service를 거치지 않고 바로 Dao에서 가져오도록 한다.
        List<MyQuizDto> myQuizzes = userProfileDao.getQuizzesByPaging(from(pageable, userInfo));
        Long totalCount = userProfileDao.getTotalCount(userInfo.userId());

        // Spring Data JPA의 Paga를 구현함으로써, last, first, totalElements 등의 정보를 자동으로 계산해준다.
        Page<MyQuizDto> page = new PageImpl<>(myQuizzes, pageable, totalCount);

        // 이 중에서 필요한 정보만 추출하여 반환한다.
        PagingResponseDto body = PagingResponseDto.from(page);

        return ResponseEntity.ok(body);

    }
}
