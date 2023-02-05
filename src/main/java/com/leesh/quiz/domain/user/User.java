package com.leesh.quiz.domain.user;

import com.leesh.quiz.domain.answer.Answer;
import com.leesh.quiz.domain.answervote.AnswerVote;
import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quizvote.QuizVote;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import com.leesh.quiz.domain.user.constant.Role;
import com.leesh.quiz.external.oauth2.Oauth2ValidationUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
@Table(name = "users", indexes = {
        @Index(columnList = "username"),
        @Index(columnList = "email"),
        @Index(columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 30, nullable = false)
    private String username;

    @Column(name = "email", length = 255, unique = true, nullable = false)
    private String email;

    @Column(name = "password", length = 255, nullable = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 10)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth2_type", nullable = false, length = 10)
    private Oauth2Type oauth2Type;

    @Column(name = "profile", length = 255, nullable = true)
    private String profile;

    @Column(name = "refresh_token", length = 255, nullable = true)
    private String refreshToken;

    @Column(name = "refresh_token_expires_in", nullable = true)
    private LocalDateTime refreshTokenExpiresIn;

    @OrderBy("id")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Quiz> quizzes = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Answer> answers = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<QuizVote> quizVotes = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<AnswerVote> answerVotes = new LinkedHashSet<>();

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
        if (!(o instanceof User user)) return false;

        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Builder
    private User(String username, String email, Role role, Oauth2Type oauth2Type, String profile) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.oauth2Type = oauth2Type;
        this.profile = profile;
    }

    /* Business Logic */
    public void updateRefreshToken(String refreshToken, LocalDateTime expiredAt) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = expiredAt;
    }

    public void isValidOauth2(Oauth2Type userInput) {
        // 외부 코드와 의존하지 않기 위해 유틸 클래스를 사용한다.
        Oauth2ValidationUtils.isValidOauth2(this.oauth2Type, userInput);
    }

    public void expireRefreshToken() {
        this.refreshTokenExpiresIn = LocalDateTime.now();
    }
}
