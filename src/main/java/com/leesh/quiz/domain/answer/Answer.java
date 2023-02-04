package com.leesh.quiz.domain.answer;

import com.leesh.quiz.domain.answervote.AnswerVote;
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
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "answer", indexes = {
        @Index(columnList = "user_id"),
        @Index(columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(nullable = false)
    private boolean deleted;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "quiz_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Quiz quiz;

    @OrderBy("id")
    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<AnswerVote> votes = new LinkedHashSet<>();

    /* Meta Data Start */
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
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
        if (!(o instanceof Answer comment)) return false;

        return id != null && id.equals(comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private Answer(String contents, User user, Quiz quiz) {
        this.contents = contents;
        this.user = user;
        this.quiz = quiz;
    }

    public static Answer of(String contents, User user, Quiz quiz) {
        return new Answer(contents, user, quiz);
    }

}
