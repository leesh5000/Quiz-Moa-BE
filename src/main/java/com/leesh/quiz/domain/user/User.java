package com.leesh.quiz.domain.user;

import com.leesh.quiz.domain.answer.Answer;
import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.quizvote.QuizVote;
import com.leesh.quiz.domain.user.constant.Role;
import com.leesh.quiz.domain.user.constant.Oauth2Type;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users", indexes = {
        @Index(columnList = "username"),
        @Index(columnList = "email"),
        @Index(columnList = "created_at")
})
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 30, unique = true, nullable = false)
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

    @Column(name = "refresh_token_expired_at", nullable = true)
    private LocalDateTime refreshTokenExpiredAt;

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

    @OrderBy("id")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Quiz> myQuizzes = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Answer> myAnswers = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<QuizVote> myQuizVotes = new LinkedHashSet<>();

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

}
