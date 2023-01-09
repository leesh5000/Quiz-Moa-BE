package com.leesh.quiz.domain.commnet;

import com.leesh.quiz.domain.like.Like;
import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Comment", indexes = {
        @Index(columnList = "user_id"),
        @Index(columnList = "createdAt")
})
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String contents;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "quiz_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Quiz quiz;

    @OrderBy("id")
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
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
        if (!(o instanceof Comment comment)) return false;

        return id != null && id.equals(comment.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
