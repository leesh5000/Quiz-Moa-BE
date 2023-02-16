package com.leesh.quiz.domain.quiz.repository;

import com.leesh.quiz.api.quiz.dto.quiz.QQuizDetailDto;
import com.leesh.quiz.api.quiz.dto.quiz.QQuizDetailDto_AuthorDto;
import com.leesh.quiz.api.quiz.dto.quiz.QQuizDetailDto_QuizVoteDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;
import com.leesh.quiz.domain.user.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

import static com.leesh.quiz.domain.quiz.QQuiz.quiz;
import static com.leesh.quiz.domain.quizvote.QQuizVote.quizVote;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;



@RequiredArgsConstructor
public class QuizDaoImpl implements QuizDao {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<QuizDetailDto> getQuizDetailByQuizId(Long id) {

//        // 퀴즈 엔티티와 연관관계가 있는 엔티티들을 패치조인을 통해서 모두 가져온 다음, QuizDetailDto로 변환하는 방법이 있음
//        List<Quiz> fetch = queryFactory
//                .selectFrom(quiz)
//                .innerJoin(quiz.user).fetchJoin()
//                .leftJoin(quiz.votes, quizVote).fetchJoin()
//                .innerJoin(quizVote.user).fetchJoin()
//                .where(
//                        quizIdEq(id),
//                        quiz.deleted.eq(false)
//                )
//                .fetch();

        // 이 방법은, 시작부터 필요한 것들만 조회하여 DTO로 가져오는 방법
        QUser author = new QUser("author");
        QUser voter = new QUser("voter");

        Map<Long, QuizDetailDto> content = queryFactory
                .from(quiz)
                    .join(quiz.user, author)
                    .leftJoin(quiz.votes, quizVote)
                    .leftJoin(quizVote.user, voter)
                .where(
                        quizIdEq(id),
                        quiz.deleted.eq(false)
                ).transform(
                    groupBy(quiz.id).as(
                        new QQuizDetailDto(
                                quiz.id,
                                quiz.title,
                                quiz.contents,
                                new QQuizDetailDto_AuthorDto(
                                        author.id,
                                        author.username,
                                        author.email
                                ),
                                list(new QQuizDetailDto_QuizVoteDto(
                                        quizVote.id,
                                        quizVote.value.intValue(),
                                        new QQuizDetailDto_AuthorDto(
                                                voter.id,
                                                voter.username,
                                                voter.email
                                        )
                                ).as("votes")),
                                quiz.createdAt,
                                quiz.modifiedAt
                        )
                    ));

        // LEFT OUTER JOIN 이므로, 퀴즈에 투표가 없는 경우에는 vote id가 null 이므로, 이를 제거해준다.
        if (content.get(id) != null) {
            content.get(id).votes()
                    .removeIf(v -> v.id() == null);
        }

        return Optional.ofNullable(content.get(id));

    }

    private BooleanExpression quizIdEq(Long id) {
        return id != null ? quiz.id.eq(id) : null;
    }
}
