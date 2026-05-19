CREATE DATABASE IF NOT EXISTS cinemasys DEFAULT CHARACTER SET utf8mb4;
USE cinemasys;

DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS screening;
DROP TABLE IF EXISTS theater;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS movie;

CREATE TABLE movie (
                       movieid  INT AUTO_INCREMENT PRIMARY KEY,
                       title    VARCHAR(100) NOT NULL,
                       genre    VARCHAR(50),
                       director VARCHAR(50),
                       rating   int
);

CREATE TABLE theater (
                         theaterid   INT AUTO_INCREMENT PRIMARY KEY,
                         name        VARCHAR(50) NOT NULL,
                         total_seats INT NOT NULL
);

CREATE TABLE screening (
                           screenid  INT AUTO_INCREMENT PRIMARY KEY,
                           movieid   INT NOT NULL,
                           theaterid INT NOT NULL,
                           showtime  DATETIME NOT NULL,
                           price     INT NOT NULL,
                           FOREIGN KEY (movieid)   REFERENCES movie(movieid),
                           FOREIGN KEY (theaterid) REFERENCES theater(theaterid)
);

CREATE TABLE customer (
                          custid INT AUTO_INCREMENT PRIMARY KEY,
                          name   VARCHAR(50) NOT NULL,
                          phone  VARCHAR(20),
                          email  VARCHAR(100),
                          age INT NOT NULL
);

CREATE TABLE reservation (
                             reservid   INT AUTO_INCREMENT PRIMARY KEY,
                             custid     INT NOT NULL,
                             screenid   INT NOT NULL,
                             seatno     VARCHAR(10) NOT NULL,
                             reservdate DATE NOT NULL,
                             FOREIGN KEY (custid)   REFERENCES customer(custid),
                             FOREIGN KEY (screenid) REFERENCES screening(screenid)
);

-- sample data
INSERT INTO movie VALUES (NULL, '인터스텔라', 'SF',   '크리스토퍼 놀란', 12);
INSERT INTO movie VALUES (NULL, '기생충',     '드라마','봉준호',         15);
INSERT INTO movie VALUES (NULL, '어벤져스',   '액션', '루소 형제',       12);

INSERT INTO theater VALUES (NULL, '1관',   50);
INSERT INTO theater VALUES (NULL, '2관',   80);
INSERT INTO theater VALUES (NULL, 'VIP관', 20);

INSERT INTO screening VALUES (NULL, 1, 1, '2026-05-20 14:00:00', 15000);
INSERT INTO screening VALUES (NULL, 1, 2, '2026-05-20 18:00:00', 15000);
INSERT INTO screening VALUES (NULL, 2, 1, '2026-05-21 11:00:00', 12000);
INSERT INTO screening VALUES (NULL, 3, 3, '2026-05-21 20:00:00', 20000);

INSERT INTO customer VALUES (NULL, '김철수', '010-1111-2222', 'kim@email.com', 15);
INSERT INTO customer VALUES (NULL, '이영희', '010-3333-4444', 'lee@email.com', 22);
INSERT INTO customer VALUES (NULL, '박민준', '010-5555-6666', 'park@email.com', 11);

INSERT INTO reservation VALUES (NULL, 1, 1, 'A1', CURDATE());
INSERT INTO reservation VALUES (NULL, 2, 1, 'A2', CURDATE());
INSERT INTO reservation VALUES (NULL, 3, 2, 'B1', CURDATE());
INSERT INTO reservation VALUES (NULL, 1, 3, 'C1', CURDATE());
INSERT INTO reservation VALUES (NULL, 2, 4, 'D1', CURDATE());