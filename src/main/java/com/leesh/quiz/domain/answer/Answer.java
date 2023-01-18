package com.leesh.quiz.domain.answer;

import com.leesh.quiz.domain.answervote.AnswerVote;
import com.leesh.quiz.domain.quiz.Quiz;
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
@Table(name = "Answer", indexes = {
        @Index(columnList = "user_id"),
        @Index(columnList = "createdAt")
})
@Entity
public class Answer {

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
    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<AnswerVote> votes = new LinkedHashSet<>();

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