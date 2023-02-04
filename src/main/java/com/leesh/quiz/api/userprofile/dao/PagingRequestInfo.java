package com.leesh.quiz.api.userprofile.dao;

import com.leesh.quiz.global.constant.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MyBatis에서 사용할 페이징 정보를 담는 클래스
 * <p>
 * {@link org.springframework.data.domain.Pageable}에서 orders의 getter가 없어서 Mybatis가 값을 바인딩 할 수 없기 때문에 사용
 * </p>
 * @param sorted
 * @param orders
 * @param offset
 * @param pageSize
 */
public record PagingRequestInfo(boolean sorted, List<Sort.Order> orders, long offset, int pageSize, Long userId) {

    public static PagingRequestInfo from(Pageable pageable, UserInfo userInfo) {
        return new PagingRequestInfo(
                pageable.getSort().isSorted(),
                pageable.getSort().stream().collect(Collectors.toList()),
                pageable.getOffset(),
                pageable.getPageSize(),
                userInfo.userId()
        );
    }

}
