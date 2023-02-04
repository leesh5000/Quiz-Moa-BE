package com.leesh.quiz.api.userprofile.service;

import com.leesh.quiz.api.userprofile.dao.PagingRequestInfo;
import com.leesh.quiz.api.userprofile.dao.UserProfileDao;
import com.leesh.quiz.api.userprofile.dto.MyQuizDto;
import com.leesh.quiz.api.userprofile.dto.PagingResponseDto;
import com.leesh.quiz.global.constant.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.leesh.quiz.api.userprofile.dao.PagingRequestInfo.from;

@RequiredArgsConstructor
@Service
public class UserProfileService {

    private final UserProfileDao userProfileDao;

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

}
