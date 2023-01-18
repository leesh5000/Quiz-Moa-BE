package com.leesh.quiz.web.dto;

import com.leesh.quiz.domain.comment.Comment;
import com.leesh.quiz.domain.like.Like;
import com.leesh.quiz.domain.quiz.Quiz;

import java.util.Set;

public interface FindMyQuizDto {

    record Response(Long id, String title, String contents, String author, Long createdAt, Long modifiedAt,
                    Set<Comment> comments, Set<Like> likes) {

        public static Response from(Quiz entity) {
            return new Response(
                    entity.getId(),
                    entity.getTitle(),
                    entity.getContents(),
                    entity.getUser().getNickname(),
                    entity.getCreatedAt(),
                    entity.getModifiedAt(),
                    entity.getComments(),
                    entity.getLikes()
            );
        }

    }

}
