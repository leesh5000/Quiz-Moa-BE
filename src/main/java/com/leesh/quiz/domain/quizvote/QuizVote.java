package com.leesh.quiz.domain.quizvote;

import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "quiz_vote", indexes = {
        @Index(columnList = "user_id"),
        @Index(columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class QuizVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "quiz_id", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Quiz quiz;

    @Column(name = "value", nullable = false)
    private byte value;

    /* Meta Data Start */
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false, length = 255)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by", nullable = false, length = 255)
    private String modifiedBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;
    /* Meta Data End */


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuizVote like)) return false;

        return id != null && id.equals(like.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private QuizVote(User user, Quiz quiz, byte value) {
        this.user = user;
        this.quiz = quiz;
        this.value = value;
    }

    public static QuizVote of(User user, Quiz quiz, byte value) {
        return new QuizVote(user, quiz, value);
    }
}
