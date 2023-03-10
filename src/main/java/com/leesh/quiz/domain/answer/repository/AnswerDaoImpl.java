package com.leesh.quiz.domain.answer.repository;

import com.leesh.quiz.api.quiz.dto.quiz.QQuizDetailDto_AnswerDto;
import com.leesh.quiz.api.quiz.dto.quiz.QQuizDetailDto_AnswerVoteDto;
import com.leesh.quiz.api.quiz.dto.quiz.QQuizDetailDto_AuthorDto;
import com.leesh.quiz.api.quiz.dto.quiz.QuizDetailDto;
import com.leesh.quiz.api.userprofile.dto.answer.QUserAnswerDto;
import com.leesh.quiz.api.userprofile.dto.answer.QUserAnswerDto_AuthorDto;
import com.leesh.quiz.api.userprofile.dto.answer.UserAnswerDto;
import com.leesh.quiz.api.userprofile.dto.user.QUserProfileDto_Answers;
import com.leesh.quiz.api.userprofile.dto.user.UserProfileDto;
import com.leesh.quiz.domain.user.QUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.leesh.quiz.domain.answer.QAnswer.answer;
import static com.leesh.quiz.domain.answervote.QAnswerVote.answerVote;
import static com.leesh.quiz.domain.quiz.QQuiz.quiz;
import static com.leesh.quiz.domain.user.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerDaoImpl implements AnswerDao {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserAnswerDto> getUserAnswersByPaging(Long userId, Pageable pageable) {

        List<UserAnswerDto> content = queryFactory
                .select(new QUserAnswerDto(
                        answer.id,
                        answer.contents,
                        quiz.id.as("quizId"),
                        new QUserAnswerDto_AuthorDto(
                                user.id.as("id"),
                                user.username.as("username"),
                                user.email.as("email")
                        ),
                        answerVote.value.intValue().sum().as("totalVotesSum"),
                        answer.createdAt,
                        answer.modifiedAt
                ))
                .from(answer)
                    .innerJoin(answer.user, user)
                    .innerJoin(answer.quiz, quiz)
                    .leftJoin(answer.votes, answerVote)
                .where(
                        user.id.eq(userId),
                        answer.deleted.eq(false)
                )
                .groupBy(answer.id)
                .orderBy(getOrderBy(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount = queryFactory
//                .select(Wildcard.count) // select count(*)
                .select(answer.count()) // select count(answer.id)
                .from(answer)
                .where(
                        user.id.eq(userId),
                        answer.deleted.eq(false)
                );

        // PageableExecutionUtils.getPage()??? count ????????? ?????? ???????????? ????????????, ????????? ?????? ??? count Query??? ????????????.
        // 1. ????????? ??????????????????, ????????? ???????????? ????????? ????????? ?????? ?????? ???
        // 2. ????????? ????????? ??? ???, (????????? ???????????????, ????????? ????????? * (?????? ????????? - 1) + ????????? ????????? = ?????? ?????????)
        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

    @Override
    public Optional<List<QuizDetailDto.AnswerDto>> getAnswersWithVoteByQuizId(Long quizId) {

        // Hibernate 6.x?????? HibernateHandler ?????? DefaultQueryHandler??? ???????????? ?????? ????????? JPAQueryFactory??? ?????? ???????????? ???????????????
        // https://github.com/querydsl/querydsl/issues/3428 ??????

        QUser author = new QUser("author");
        QUser voter = new QUser("voter");

        List<QuizDetailDto.AnswerDto> content = queryFactory
                .from(answer)
                    .innerJoin(answer.user, author)
                    .innerJoin(answer.quiz, quiz)
                    .leftJoin(answer.votes, answerVote)
                    .leftJoin(answerVote.user, voter)
                .where(
                        quiz.id.eq(quizId),
                        answer.deleted.eq(false)
                )
                .orderBy(answer.createdAt.asc())
                .transform(
                    groupBy(answer.id).list(
                        new QQuizDetailDto_AnswerDto(
                                answer.id,
                                answer.contents,
                                new QQuizDetailDto_AuthorDto(
                                        author.id,
                                        author.username,
                                        author.email
                                ),
                                list(new QQuizDetailDto_AnswerVoteDto(
                                                answerVote.id,
                                                answerVote.value.intValue(),
                                                new QQuizDetailDto_AuthorDto(
                                                        voter.id,
                                                        voter.username,
                                                        voter.email
                                                )
                                ).as("votes")),
                                answer.createdAt,
                                answer.modifiedAt
                        )
                ));

        // LEFT OUTER JOIN ?????????, ????????? ????????? ?????? ???????????? vote id??? null ?????????, ?????? ???????????????.
        if (!content.isEmpty()) {
            for (var answerDto : content) {
                answerDto.votes().removeIf(v -> v.id() == null);
            }
        }

        return Optional.ofNullable(
                content.size() == 0 ? null : content);
    }

    @Override
    public Optional<UserProfileDto.Answers> getUserAnswerCountWithVotesSum(Long userId) {

        UserProfileDto.Answers contents = queryFactory
                .select(new QUserProfileDto_Answers(
                        answer.id.countDistinct().intValue().as("totalCount"),
                        answerVote.value.intValue().sum().as("totalVotesSum")
                ))
                .from(answer)
                .leftJoin(answer.votes, answerVote)
                .where(
                        answer.user.id.eq(userId),
                        answer.deleted.eq(false)
                )
                .fetchOne();

        return Optional.ofNullable(contents);
    }

    private OrderSpecifier<?>[] getOrderBy(Sort sort) {

        final List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {

            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "totalVotesSum" -> orders.add(new OrderSpecifier(direction, answerVote.value.intValue().sum()));
                case "createdAt" -> orders.add(new OrderSpecifier(direction, answer.createdAt));
                case "modifiedAt" -> orders.add(new OrderSpecifier(direction, answer.modifiedAt));
                default -> {}
            }

        }

        return orders.toArray(OrderSpecifier[]::new);
    }

}
