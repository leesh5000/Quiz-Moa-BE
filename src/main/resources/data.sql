insert into users (id, nickname, email, password, role, created_at, modified_at) values (1, 'test1', 'test1@gmail.com', '$2a$10$1K.O6dtG81gh7RQwbpAS9.J0uFENxzM/w2/22lUCONOZT8GjkeYR6', 'USER', unix_timestamp(), unix_timestamp());
insert into users (id, nickname, email, password, role, created_at, modified_at) values (2, 'test2', 'test2@gmail.com', '$2a$10$1K.O6dtG81gh7RQwbpAS9.J0uFENxzM/w2/22lUCONOZT8GjkeYR6', 'USER', unix_timestamp(), unix_timestamp());
insert into users (id, nickname, email, password, role, created_at, modified_at) values (3, 'test3', 'test3@gmail.com', '$2a$10$1K.O6dtG81gh7RQwbpAS9.J0uFENxzM/w2/22lUCONOZT8GjkeYR6', 'USER', unix_timestamp(), unix_timestamp());
insert into users (id, nickname, email, password, role, created_at, modified_at) values (4, 'test4', 'test4@gmail.com', '$2a$10$1K.O6dtG81gh7RQwbpAS9.J0uFENxzM/w2/22lUCONOZT8GjkeYR6', 'USER', unix_timestamp(), unix_timestamp());
insert into users (id, nickname, email, password, role, created_at, modified_at) values (5, 'test5', 'test5@gmail.com', '$2a$10$1K.O6dtG81gh7RQwbpAS9.J0uFENxzM/w2/22lUCONOZT8GjkeYR6', 'USER', unix_timestamp(), unix_timestamp());

insert into quiz (id, user_id, title, contents, created_at, modified_at) values (1, 1, 'quiz1', 'quiz1 contents', unix_timestamp(), unix_timestamp());

insert into comment (id, user_id, quiz_id, contents, created_at, modified_at) values (1, 1, 1, 'comment1 contents', unix_timestamp(), unix_timestamp());
insert into comment (id, user_id, quiz_id, contents, created_at, modified_at) values (2, 2, 1, 'comment2 contents', unix_timestamp(), unix_timestamp());
insert into comment (id, user_id, quiz_id, contents, created_at, modified_at) values (3, 3, 1, 'comment3 contents', unix_timestamp(), unix_timestamp());
insert into comment (id, user_id, quiz_id, contents, created_at, modified_at) values (4, 4, 1, 'comment4 contents', unix_timestamp(), unix_timestamp());
insert into comment (id, user_id, quiz_id, contents, created_at, modified_at) values (5, 5, 1, 'comment5 contents', unix_timestamp(), unix_timestamp());

insert into likes (id, user_id, quiz_id, comment_id, created_at, modified_at) values (1, 1, 1, null, unix_timestamp(), unix_timestamp());
insert into likes (id, user_id, quiz_id, comment_id, created_at, modified_at) values (2, 2, 1, null, unix_timestamp(), unix_timestamp());
insert into likes (id, user_id, quiz_id, comment_id, created_at, modified_at) values (3, 1, null, 1, unix_timestamp(), unix_timestamp());
insert into likes (id, user_id, quiz_id, comment_id, created_at, modified_at) values (4, 3, null, 2, unix_timestamp(), unix_timestamp());

insert into quiz (id, user_id, title, contents, created_at, modified_at) values (2, 1, 'quiz2', 'quiz2 contents', unix_timestamp(), unix_timestamp());