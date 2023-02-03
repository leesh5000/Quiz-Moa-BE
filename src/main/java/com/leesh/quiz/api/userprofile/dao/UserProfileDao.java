package com.leesh.quiz.api.userprofile.dao;

import com.leesh.quiz.api.userprofile.dto.MyQuizDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 화면에 특화된 쿼리를 처리하기 위한 데이터베이스 접근 객체 <br>
 * 도메인 기능을 처리를 위해 데이터베이스에 접근하는 <code>Repository</code> 객체와의 구분을 위해 <code>Dao</code>라는 이름을 사용
 * </p>
 */
@Mapper
@Repository
public interface UserProfileDao {

    List<MyQuizDto> getQuizzesByPaging(UserProfilePagingInfo pagingInfo);

    Long getTotalCount(@Param("userId") Long userId);

}
