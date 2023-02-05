package com.leesh.quiz.domain.answer;

import com.leesh.quiz.api.userprofile.dto.MyAnswerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnswerDao {

    Page<MyAnswerDto> getMyAnswersByPaging(Long userId, Pageable pageable);

}
