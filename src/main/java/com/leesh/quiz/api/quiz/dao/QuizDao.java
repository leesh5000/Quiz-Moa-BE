package com.leesh.quiz.api.quiz.dao;

import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDto;
import com.leesh.quiz.global.constant.PagingRequestInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuizDao {

    List<QuizDto> getQuizzesByPaging(PagingRequestInfo pagingInfo);

    Long getTotalCount();

    QuizDetailDto getQuizDetailById(Long id);

}
