TRUNCATE TABLE user;


INSERT INTO user (id, username, email, password, salt, sex, create_time, modify_time)
VALUES
(1, 'user1', '1@qq.com', 'pw1', 'salt1', 1, now(), now()),
(2, 'user2', '2@qq.com', 'pw2', 'salt2', 1, now(), now()),
(3, 'user3', '3@qq.com', 'pw3', 'salt3', 2, now(), now()),
(4, 'user4', '4@qq.com', 'pw4', 'salt4', 2, now(), now()),
(5, 'user5', '5@qq.com', 'pw5', 'salt5', 2, now(), now());
