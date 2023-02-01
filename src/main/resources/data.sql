insert into users (id, username, email, password, role, oauth2_type, profile, refresh_token, refresh_token_expires_in, created_by, modified_by, created_at, modified_at)
values (1, 'test1', 'test1@gmail.com', '', 'User', 'KAKAO', '', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjMxODkxLCJleHAiOjE2NzY0NDE0OTEsInVzZXJJZCI6MX0.bqOExH6TeDxkzxry3Ytm3BzaEs3lCq0J2BZnRB1FoPEbLInWPWTMeTHuXnBxLYu6FcF-w0u-qyuXZZWoIV-GXQ', '2023-02-15 15:11:31.239', '/api/oauth2/login', '/api/oauth2/login', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into quiz (id, user_id, title, contents, created_by, modified_by, created_at, modified_at)
values (1, 1, 'test1의 퀴즈', 'test1 퀴즈는 정말 어렵다.', '/api/quiz', '/api/quiz', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer (id, user_id, quiz_id, contents, created_by, modified_by, created_at, modified_at)
values (1, 1, 1, 'test1의 답변', '/api/answers', '/api/answers', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into quiz_vote (id, user_id, quiz_id, value, created_by, modified_by, created_at, modified_at)
values (1, 1, 1, true, '/api/quizzes/votes', '/api/quizzes/votes', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer_vote (id, user_id, answer_id, value, created_by, modified_by, created_at, modified_at)
values (1, 1, 1, true, '/api/answers/votes', '/api/answers/votes', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');
