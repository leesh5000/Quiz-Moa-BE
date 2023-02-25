package com.leesh.quiz.domain.user;

import com.leesh.quiz.domain.answer.Answer;
import com.leesh.quiz.domain.answervote.AnswerVote;
import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quizvote.QuizVote;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import com.leesh.quiz.domain.user.constant.Role;
import com.leesh.quiz.external.oauth2.Oauth2Attributes;
import com.leesh.quiz.external.oauth2.Oauth2ValidationUtils;
import com.leesh.quiz.global.error.ErrorCode;
import com.leesh.quiz.global.error.exception.BusinessException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Column(nullable = false)
    private boolean deleted = false;

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
    private final List<Quiz> quizzes = new ArrayList<>();

    @OrderBy("id")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Answer> answers = new ArrayList<>();

    @OrderBy("id")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<QuizVote> quizVotes = new ArrayList<>();

    @OrderBy("id")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<AnswerVote> answerVotes = new ArrayList<>();

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
        Oauth2ValidationUtils.isValidOauth2Type(this.oauth2Type, userInput);
    }

    public void expireRefreshToken() {
        this.refreshTokenExpiresIn = LocalDateTime.now();
    }

    public void editUsername(String username, Long userId) {
        // 요청을 보낸 유저가 이 리소스의 유저인지 검증한다.
        validate(userId);
        this.username = username;
    }

    public void disable(Long userId) {
        // 요청을 보낸 유저가 이 리소스의 유저인지 검증한다.
        validate(userId);
        this.deleted = true;
    }

    public void enable(Oauth2Attributes oauth2Attributes) {
        // 요청을 보낸 유저가 이 리소스의 유저인지 검증한다.
        this.deleted = false;
        // 유저를 최초 상태로 초기화한다.
        initialize(oauth2Attributes);
    }

    private void initialize(Oauth2Attributes oauth2Attributes) {
        this.email = oauth2Attributes.getEmail();
        this.username = oauth2Attributes.getName();
        this.profile = oauth2Attributes.getProfile();
    }

    private void validate(Long inputId) {
        if (!Objects.equals(this.id, inputId)) {
            throw new BusinessException(ErrorCode.NOT_ACCESSIBLE_USER);
        }
    }
}
