package com.leesh.quiz.domain.quiz.repository;

import com.leesh.quiz.api.quiz.dto.quiz.QQuizDetailDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.leesh.quiz.domain.quiz.QQuiz.quiz;
import static com.leesh.quiz.domain.quizvote.QQuizVote.quizVote;
import static com.leesh.quiz.domain.user.QUser.user;

@RequiredArgsConstructor
public class QuizDaoImpl implements QuizDao {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<QuizDetailDto> getQuizDetail(Long quizId) {

        QuizDetailDto content = queryFactory
                .select(new QQuizDetailDto(
                        quiz.id,
                        quiz.title,
                        quiz.contents,
                        user.email.as("author"),
                        quizVote.value.intValue().sum().as("votes"),
                        quiz.createdAt,
                        quiz.modifiedAt
                ))
                .from(quiz)
                .innerJoin(quiz.user, user)
                .leftJoin(quiz.votes, quizVote)
                .where(
                        quizIdEq(quizId),
                        quiz.deleted.eq(false)
                )
                .fetchOne();

        return Optional.ofNullable(content);
    }

    private BooleanExpression quizIdEq(Long quizId) {
        return quiz.id.eq(quizId);
    }
}
