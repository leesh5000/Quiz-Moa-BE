package com.leesh.quiz.api.quiz.dao;

import com.leesh.quiz.api.quiz.dto.quiz.QuizDto;
import com.leesh.quiz.domain.answer.repository.AnswerDaoImpl;
import com.leesh.quiz.global.constant.PagingRequestInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 화면에 특화된 쿼리를 처리하기 위한 데이터베이스 접근 객체
 * <p>
 * 도메인 기능을 처리를 위해 데이터베이스에 접근하는 <code>Repository</code> 객체와의 구분을 위해 <code>Dao</code>라는 이름을 사용
 * 기본적으로 {@link AnswerDaoImpl}과 같이 QueryDsl을 사용하지만, from 절의 서브쿼리 등과 같이 QueryDsl로 처리하기 어려운 경우에는 MyBatis를 사용한다.
 * </p>
 */
@Mapper
@Repository
@Transactional(readOnly = true)
public interface QuizDao {

    List<QuizDto> getQuizzesByPaging(PagingRequestInfo pagingInfo);

    Long getTotalCount();

    Long getTotalCountByUserId(Long userId);

}
