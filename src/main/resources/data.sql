insert into users (id, username, email, password, role, oauth2_type, profile, refresh_token, refresh_token_expires_in, created_by, modified_by, created_at, modified_at)
values (1, 'test1', 'test1@gmail.com', null, 'USER', 'KAKAO', '', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjMxODkxLCJleHAiOjE2NzY0NDE0OTEsInVzZXJJZCI6MX0.bqOExH6TeDxkzxry3Ytm3BzaEs3lCq0J2BZnRB1FoPEbLInWPWTMeTHuXnBxLYu6FcF-w0u-qyuXZZWoIV-GXQ', '2023-02-15 15:11:31.239', '/api/oauth2/login', '/api/oauth2/login', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into users (id, username, email, password, role, oauth2_type, profile, refresh_token, refresh_token_expires_in, created_by, modified_by, created_at, modified_at)
values (2, 'test2', 'test2@gmail.com', null, 'USER', 'NAVER', '', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjMxODkxLCJleHAiOjE2NzY0NDE0OTEsInVzZXJJZCI6MX0.bqOExH6TeDxkzxry3Ytm3BzaEs3lCq0J2BZnRB1FoPEbLInWPWTMeTHuXnBxLYu6FcF-w0u-qyuXZZWoIV-GXQ', '2023-02-15 15:11:31.239', '/api/oauth2/login', '/api/oauth2/login', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into users (id, username, email, password, role, oauth2_type, profile, refresh_token, refresh_token_expires_in, created_by, modified_by, created_at, modified_at)
values (3, 'test3', 'test3@gmail.com', null, 'USER', 'GOOGLE', '', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjMxODkxLCJleHAiOjE2NzY0NDE0OTEsInVzZXJJZCI6MX0.bqOExH6TeDxkzxry3Ytm3BzaEs3lCq0J2BZnRB1FoPEbLInWPWTMeTHuXnBxLYu6FcF-w0u-qyuXZZWoIV-GXQ', '2023-02-15 15:11:31.239', '/api/oauth2/login', '/api/oauth2/login', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into users (id, username, email, password, role, oauth2_type, profile, refresh_token, refresh_token_expires_in, created_by, modified_by, created_at, modified_at)
values (4, 'test4', 'test4@gmail.com', null, 'USER', 'GOOGLE', '', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MjMxODkxLCJleHAiOjE2NzY0NDE0OTEsInVzZXJJZCI6MX0.bqOExH6TeDxkzxry3Ytm3BzaEs3lCq0J2BZnRB1FoPEbLInWPWTMeTHuXnBxLYu6FcF-w0u-qyuXZZWoIV-GXQ', '2023-02-15 15:11:31.239', '/api/oauth2/login', '/api/oauth2/login', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into users (id, username, email, password, role, oauth2_type, profile, refresh_token, refresh_token_expires_in, created_by, modified_by, created_at, modified_at)
values (5, 'test5', 'test5@gmail.com', null, 'USER', 'GOOGLE', '', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNjc1MzA0ODM5LCJleHAiOjE2NzY1MTQ0MzksInVzZXJJZCI6NX0.mOwqlcrNd_ksksxyiBfcex_oUnQ2_JhFgQQiaa0QIcjp-8ZtfBVcTxTBr-NeDwlemaOPVNS4L9gBnEvB77M_CA', '2023-02-16 11:27:19', '/api/oauth2/login', '/api/oauth2/login', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into quiz (id, user_id, title, contents, created_by, modified_by, created_at, modified_at)
values (1, 1, 'test1의 퀴즈 1', 'test1 퀴즈는 정말 어렵다.', '/api/quiz', '/api/quiz', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer (id, user_id, quiz_id, contents, created_by, modified_by, created_at, modified_at)
values (1, 1, 1, 'test1의 답변', '/api/answers', '/api/answers', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer (id, user_id, quiz_id, contents, created_by, modified_by, created_at, modified_at)
values (2, 2, 1, 'test1의 답변', '/api/answers', '/api/answers', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer (id, user_id, quiz_id, contents, created_by, modified_by, created_at, modified_at)
values (3, 1, 1, 'test1의 답변', '/api/answers', '/api/answers', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer (id, user_id, quiz_id, contents, created_by, modified_by, created_at, modified_at)
values (4, 3, 1, 'test1의 답변', '/api/answers', '/api/answers', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer (id, user_id, quiz_id, contents, created_by, modified_by, created_at, modified_at)
values (5, 4, 1, 'test1의 답변', '/api/answers', '/api/answers', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into quiz_vote (id, user_id, quiz_id, value, created_by, modified_by, created_at, modified_at)
values (1, 1, 1, true, '/api/quizzes/votes', '/api/quizzes/votes', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into quiz_vote (id, user_id, quiz_id, value, created_by, modified_by, created_at, modified_at)
values (2, 2, 1, true, '/api/quizzes/votes', '/api/quizzes/votes', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into quiz_vote (id, user_id, quiz_id, value, created_by, modified_by, created_at, modified_at)
values (3, 3, 1, true, '/api/quizzes/votes', '/api/quizzes/votes', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer_vote (id, user_id, answer_id, value, created_by, modified_by, created_at, modified_at)
values (1, 2, 1, true, '/api/answers/votes', '/api/answers/votes', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer_vote (id, user_id, answer_id, value, created_by, modified_by, created_at, modified_at)
values (2, 3, 1, true, '/api/answers/votes', '/api/answers/votes', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into quiz (id, user_id, title, contents, created_by, modified_by, created_at, modified_at)
values (2, 1, 'test1의 퀴즈 2', 'test1 퀴즈는 정말 어렵다.', '/api/quiz', '/api/quiz', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into quiz (id, user_id, title, contents, created_by, modified_by, created_at, modified_at)
values (3, 1, 'test1의 퀴즈 3', 'test1 퀴즈는 정말 어렵다.', '/api/quiz', '/api/quiz', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer (id, user_id, quiz_id, contents, created_by, modified_by, created_at, modified_at)
values (6, 2, 3, 'test2의 답변', '/api/answers', '/api/answers', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer (id, user_id, quiz_id, contents, created_by, modified_by, created_at, modified_at)
values (7, 3, 3, 'test3의 답변', '/api/answers', '/api/answers', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into quiz_vote (id, user_id, quiz_id, value, created_by, modified_by, created_at, modified_at)
values (4, 2, 3, true, '/api/quizzes/votes', '/api/quizzes/votes', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer_vote (id, user_id, answer_id, value, created_by, modified_by, created_at, modified_at)
values (3, 3, 6, true, '/api/answers/votes', '/api/answers/votes', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into quiz (id, user_id, title, contents, created_by, modified_by, created_at, modified_at)
values (4, 5, 'test5의 퀴즈 1', 'test5 퀴즈는 정말 어렵다.', '/api/quiz', '/api/quiz', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into quiz (id, user_id, title, contents, created_by, modified_by, created_at, modified_at)
values (5, 5, 'test5의 퀴즈 2', 'test5 퀴즈는 정말 어렵다.', '/api/quiz', '/api/quiz', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer (id, user_id, quiz_id, contents, created_by, modified_by, created_at, modified_at)
values (8, 2, 5, 'test2의 답변', '/api/answers', '/api/answers', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into answer (id, user_id, quiz_id, contents, created_by, modified_by, created_at, modified_at)
values (9, 3, 5, 'test3의 답변', '/api/answers', '/api/answers', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');

insert into quiz_vote (id, user_id, quiz_id, value, created_by, modified_by, created_at, modified_at)
values (5, 2, 5, true, '/api/quizzes/votes', '/api/quizzes/votes', '2023-02-01 15:11:31.172654', '2023-02-01 15:11:31.172654');