package com.leesh.quiz.domain.answervote;

import com.leesh.quiz.domain.answer.Answer;
import com.leesh.quiz.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Answer_Vote", indexes = {
        @Index(columnList = "user_id"),
        @Index(columnList = "createdAt")
})
@Entity
public class AnswerVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "quiz_id", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Answer answer;

    @Column(nullable = false, updatable = false)
    private Long createdAt;

    @Column(nullable = false)
    private Long modifiedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = System.currentTimeMillis();
        modifiedAt = System.currentTimeMillis();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedAt = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnswerVote like)) return false;

        return id != null && id.equals(like.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private AnswerVote(User user, Answer answer) {
        this.user = user;
        this.answer = answer;
    }

    public static AnswerVote of(User user, Answer answer) {
        return new AnswerVote(user, answer);
    }
}