package com.leesh.quiz.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.leesh.quiz.common.Constants.encoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users", indexes = {
        @Index(columnList = "username"),
        @Index(columnList = "email"),
        @Index(columnList = "createdAt")
})
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, unique = true, nullable = false)
    private String username;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

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
        if (!(o instanceof User user)) return false;

        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    private User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static User of(String email, String username, String password) {
        return new User(email, username, encoder.encode(password));
    }
}
