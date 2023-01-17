package com.leesh.quiz.domain.quiz;

import com.leesh.quiz.domain.comment.Comment;
import com.leesh.quiz.domain.like.Like;
import com.leesh.quiz.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Quiz", indexes = {
        @Index(columnList = "user_id"),
        @Index(columnList = "title"),
        @Index(columnList = "createdAt")
})
@Entity
public class Quiz {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String contents;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @OrderBy("id")
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Comment> comments = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Like> likes = new LinkedHashSet<>();

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

}
