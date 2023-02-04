package com.leesh.quiz.domain.quiz;

import com.leesh.quiz.domain.answer.Answer;
import com.leesh.quiz.domain.quizvote.QuizVote;
import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.BusinessException;
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
@Table(name = "quiz", indexes = {
        @Index(columnList = "user_id"),
        @Index(columnList = "title"),
        @Index(columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Quiz {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @OrderBy("id")
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Answer> answers = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<QuizVote> votes = new LinkedHashSet<>();

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
        if (!(o instanceof Quiz quiz)) return false;

        return id != null && id.equals(quiz.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private Quiz(String title, String contents, User user) {
        this.title = title;
        this.contents = contents;
        this.user = user;
    }

    public static Quiz of(String title, String contents, User user) {
        return new Quiz(title, contents, user);
    }

    public void edit(String title, String contents, Long userId) throws BusinessException {

        // 퀴즈 작성자인지 검증한다.
        validateQuizOwner(userId);

        this.title = title;
        this.contents = contents;
    }

    private void validateQuizOwner(Long userId) {
        if (!Objects.equals(this.user.getId(), userId)) {
            throw new BusinessException(ErrorCode.IS_NOT_QUIZ_OWNER);
        }
    }
}
