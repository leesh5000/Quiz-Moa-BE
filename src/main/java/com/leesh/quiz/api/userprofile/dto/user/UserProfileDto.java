package com.leesh.quiz.api.userprofile.dto.user;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record UserProfileDto(Long id, String username, String email, String profileImageUrl,
                             Quizzes quizzes, Answers answers) {

    public record Quizzes(int totalCount, int totalVotesSum) {

        @QueryProjection
        public Quizzes {
        }
    }

    public record Answers(int totalCount, int totalVotesSum) {

        @QueryProjection
        public Answers {
        }
    }

}
