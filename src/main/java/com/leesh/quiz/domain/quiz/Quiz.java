package com.leesh.quiz.domain.quiz;

import com.leesh.quiz.domain.commnet.Comment;
import com.leesh.quiz.domain.like.Like;
import com.leesh.quiz.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
    private final Set<Comment> comments = ConcurrentHashMap.newKeySet();

    @OrderBy("id")
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Like> likes = ConcurrentHashMap.newKeySet();

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
        return id.hashCode();
    }

}
