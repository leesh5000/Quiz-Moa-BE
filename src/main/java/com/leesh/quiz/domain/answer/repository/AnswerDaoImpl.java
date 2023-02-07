package com.leesh.quiz.domain.answer.repository;

import com.leesh.quiz.api.quiz.dto.quiz.QQuizDetailDto_AnswerDto;
import com.leesh.quiz.api.quiz.dto.quiz.QQuizDetailDto_AnswerVoteDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;
import com.leesh.quiz.api.userprofile.dto.answer.MyAnswerDto;
import com.leesh.quiz.api.userprofile.dto.answer.QMyAnswerDto;
import com.leesh.quiz.domain.user.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.leesh.quiz.domain.answer.QAnswer.answer;
import static com.leesh.quiz.domain.answervote.QAnswerVote.answerVote;
import static com.leesh.quiz.domain.quiz.QQuiz.quiz;
import static com.leesh.quiz.domain.user.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

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
                        answer.deleted.eq(false)
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
                        answer.deleted.eq(false)
                );

        // PageableExecutionUtils.getPage()는 count 쿼리에 대해 최적화를 해주는데, 다음과 같을 때 count Query를 생략한다.
        // 1. 첫번째 페이지이면서, 컨텐츠 사이즈가 페이지 사이즈 보다 작을 때
        // 2. 마지막 페이지 일 때, (마지막 페이지이면, 페이즈 사이즈 * (현재 페이지 - 1) + 컨텐츠 사이즈 = 전체 사이즈)
        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

    @Override
    public Optional<List<QuizDetailDto.AnswerDto>> getAnswersWithVoteByQuizId(Long quizId) {

        // Hibernate 6.x에서 HibernateHandler 대신 DefaultQueryHandler를 사용하는 현상 때문에 JPAQueryFactory를 직접 생성해서 사용해야함
        // https://github.com/querydsl/querydsl/issues/3428 참고

        QUser author = new QUser("author");
        QUser voter = new QUser("voter");

        List<QuizDetailDto.AnswerDto> content = queryFactory
                .from(answer)
                    .innerJoin(answer.user, author)
                    .innerJoin(answer.quiz, quiz)
                    .leftJoin(answer.votes, answerVote)
                    .leftJoin(answerVote.user, voter)
                .where(
                        quizIdEq(quizId),
                        answer.deleted.eq(false)
                )
                .transform(
                    groupBy(answer.id).list(
                        new QQuizDetailDto_AnswerDto(
                                answer.id,
                                answer.contents,
                                author.id.as("authorId"),
                                author.email.as("author"),
                                list(new QQuizDetailDto_AnswerVoteDto(
                                                answerVote.id,
                                                answerVote.value.intValue(),
                                                voter.id.as("voterId"),
                                                voter.email.as("voter")
                                ).as("votes")),
                                answer.createdAt,
                                answer.modifiedAt
                        )
                ));

        // LEFT OUTER JOIN 이므로, 퀴즈에 투표가 없는 경우에는 vote id가 null 이므로, 이를 제거해준다.
        if (!content.isEmpty()) {
            for (var answerDto : content) {
                answerDto.votes().removeIf(v -> v.id() == null);
            }
        }

        return Optional.ofNullable(
                content.size() == 0 ? null : content);
    }

    private BooleanExpression quizIdEq(Long quizId) {
        return quiz.id.eq(quizId);
    }

    private BooleanExpression userIdEq(Long userId) {
        return user.id.eq(userId);
    }

}