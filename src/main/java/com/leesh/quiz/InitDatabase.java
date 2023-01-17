package com.leesh.quiz;

import com.leesh.quiz.domain.comment.Comment;
import com.leesh.quiz.domain.like.Like;
import com.leesh.quiz.domain.quiz.Quiz;
import com.leesh.quiz.domain.user.User;
import com.leesh.quiz.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InitDatabase implements ApplicationRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        for (int i = 0; i < 20; i++) {

            String username = "test" + i;
            String email = "test" + i + "@gmail.com";
            String password = passwordEncoder.encode("1234ab!@#$");

            User user = User.of(username, email, password);

            for (int j = 0; j < 20; j++) {

                // User1 퀴즈 생성
                Quiz quiz = Quiz.of(username + "'s title" + j, username + "'s contents" + j, user);
                user.getQuizzes().add(quiz);

                // User1 퀴즈에 댓글 작성
                Comment comment = Comment.of("comment's contents", user, quiz);
                quiz.getComments().add(comment);
                user.getComments().add(comment);

                // User1 퀴즈에 좋아요
                if (j < 10) {
                    Like like = Like.of(user, quiz, null);
                    user.getLikes().add(like);
                } else {

                    // User1 댓글에 좋아요
                    Like like = Like.of(user, null, comment);
                    user.getLikes().add(like);
                }
            }

            userRepository.save(user);
        }

    }
}
