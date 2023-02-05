package com.leesh.quiz.domain.answer;

import com.leesh.quiz.api.userprofile.dto.answer.MyAnswerDto;
import com.leesh.quiz.api.userprofile.dto.answer.QMyAnswerDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.leesh.quiz.domain.answer.QAnswer.answer;
import static com.leesh.quiz.domain.answervote.QAnswerVote.answerVote;
import static com.leesh.quiz.domain.quiz.QQuiz.quiz;
import static com.leesh.quiz.domain.user.QUser.user;

@RequiredArgsConstructor
public class AnswerDaoImpl implements AnswerDao {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MyAnswerDto> getMyAnswersByPaging(Long userId, Pageable pageable) {

        List<MyAnswerDto> content = queryFactory
                .select(new QMyAnswerDto(
                        answer.id,
                        answer.contents,
                        quiz.id.as("quizId"),
                        user.email.as("author"),
                        answerVote.value.intValue().sum().as("votes"),
                        answer.createdAt,
                        answer.modifiedAt
                ))
                .from(answer)
                .innerJoin(answer.user, user)
                .innerJoin(answer.quiz, quiz)
                .leftJoin(answer.votes, answerVote)
                .where(
                        userIdEq(userId),
                        deletedEq(false)
                )
                .groupBy(answer.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount = queryFactory
//                .select(Wildcard.count) // select count(*)
                .select(answer.count()) // select count(answer.id)
                .from(answer)
                .where(
                        userIdEq(userId),
                        deletedEq(false)
                );

        // PageableExecutionUtils.getPage()는 count 쿼리에 대해 최적화를 해주는데, 다음과 같을 때 count Query를 생략한다.
        // 1. 첫번째 페이지이면서, 컨텐츠 사이즈가 페이지 사이즈 보다 작을 때
        // 2. 마지막 페이지 일 때, (마지막 페이지이면, 페이즈 사이즈 * (현재 페이지 - 1) + 컨텐츠 사이즈 = 전체 사이즈)
        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

    private BooleanExpression userIdEq(Long userId) {
        return user.id.eq(userId);
    }

    private BooleanExpression deletedEq(boolean deleted) {
        return answer.deleted.eq(deleted);
    }
}
