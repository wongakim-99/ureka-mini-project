CREATE DATABASE IF NOT EXISTS cinemasys DEFAULT CHARACTER SET utf8mb4;
USE cinemasys;

DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS screening;
DROP TABLE IF EXISTS theater;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS movie;

CREATE TABLE movie (
    movieid  INT AUTO_INCREMENT PRIMARY KEY,
    title    VARCHAR(200) NOT NULL,
    genre    VARCHAR(100),
    director VARCHAR(100),
    rating   VARCHAR(50),
    runtime  INT DEFAULT 0,
    UNIQUE(title)
);

CREATE TABLE theater (
    theaterid   INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    total_seats INT NOT NULL,
    UNIQUE(name)
);

CREATE TABLE screening (
    screenid  INT AUTO_INCREMENT PRIMARY KEY,
    movieid   INT NOT NULL,
    theaterid INT NOT NULL,
    showtime  DATETIME NOT NULL,
    price     INT NOT NULL,
    UNIQUE(theaterid, showtime),
    FOREIGN KEY (movieid)   REFERENCES movie(movieid),
    FOREIGN KEY (theaterid) REFERENCES theater(theaterid)
);

CREATE TABLE customer (
    custid INT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(50) NOT NULL,
    phone  VARCHAR(20) NOT NULL,
    email  VARCHAR(100),
    UNIQUE(phone),
    age int
);

CREATE TABLE reservation (
    reservid   INT AUTO_INCREMENT PRIMARY KEY,
    custid     INT NOT NULL,
    screenid   INT NOT NULL,
    seatno     VARCHAR(10) NOT NULL,
    reservdate DATE NOT NULL,
    UNIQUE(screenid, seatno),
    FOREIGN KEY (custid)    REFERENCES customer(custid),
    FOREIGN KEY (screenid)  REFERENCES screening(screenid)
);

INSERT INTO theater VALUES (NULL, '1관',    100);
INSERT INTO theater VALUES (NULL, '2관',     80);
INSERT INTO theater VALUES (NULL, 'VIP관',   30);
INSERT INTO theater VALUES (NULL, 'IMAX관', 200);
INSERT INTO theater VALUES (NULL, '4DX관',   80);

INSERT INTO customer VALUES (NULL, '김가원', '010-1234-5678', 'gawon@email.com',    25);
INSERT INTO customer VALUES (NULL, '이민준', '010-2345-6789', 'minjun@email.com',   17);
INSERT INTO customer VALUES (NULL, '박지수', '010-3456-7890', 'jisu@email.com',     30);
INSERT INTO customer VALUES (NULL, '김철수', '010-1111-2222', 'chulsoo@email.com',  14);
INSERT INTO customer VALUES (NULL, '이영희', '010-3333-4444', 'younghee@email.com', 22);
INSERT INTO customer VALUES (NULL, '박민준', '010-5555-6666', 'pminjun@email.com',  19);

INSERT INTO movie (movieid, title, genre, director, rating, runtime) VALUES (NULL, '범죄도시4',      '액션',        '허명행', '15세이상관람가', 109);
INSERT INTO movie (movieid, title, genre, director, rating, runtime) VALUES (NULL, '파묘',           '공포/미스터리', '장재현', '15세이상관람가', 134);
INSERT INTO movie (movieid, title, genre, director, rating, runtime) VALUES (NULL, '인사이드 아웃 2', '애니메이션',  '켈시 만', '전체관람가',    100);
INSERT INTO movie (movieid, title, genre, director, rating, runtime) VALUES (NULL, '베테랑2',        '액션/범죄',   '류승완', '15세이상관람가', 120);
INSERT INTO movie (movieid, title, genre, director, rating, runtime) VALUES (NULL, '소풍',           '드라마',      '허진호', '12세이상관람가',  96);

INSERT INTO screening (screenid, movieid, theaterid, showtime,              price) VALUES (NULL, 1, 1, '2026-05-21 10:00:00', 12000);
INSERT INTO screening (screenid, movieid, theaterid, showtime,              price) VALUES (NULL, 1, 2, '2026-05-21 13:00:00', 12000);
INSERT INTO screening (screenid, movieid, theaterid, showtime,              price) VALUES (NULL, 2, 3, '2026-05-21 11:00:00', 18000);
INSERT INTO screening (screenid, movieid, theaterid, showtime,              price) VALUES (NULL, 2, 1, '2026-05-21 16:00:00', 12000);
INSERT INTO screening (screenid, movieid, theaterid, showtime,              price) VALUES (NULL, 3, 4, '2026-05-21 14:00:00', 20000);
INSERT INTO screening (screenid, movieid, theaterid, showtime,              price) VALUES (NULL, 3, 2, '2026-05-22 10:00:00', 12000);
INSERT INTO screening (screenid, movieid, theaterid, showtime,              price) VALUES (NULL, 4, 5, '2026-05-22 13:00:00', 15000);
INSERT INTO screening (screenid, movieid, theaterid, showtime,              price) VALUES (NULL, 4, 1, '2026-05-22 17:00:00', 12000);
INSERT INTO screening (screenid, movieid, theaterid, showtime,              price) VALUES (NULL, 5, 2, '2026-05-22 15:00:00', 12000);
INSERT INTO screening (screenid, movieid, theaterid, showtime,              price) VALUES (NULL, 5, 3, '2026-05-23 12:00:00', 18000);

INSERT INTO reservation (reservid, custid, screenid, seatno, reservdate) VALUES (NULL, 1, 1,  'A01', '2026-05-20');
INSERT INTO reservation (reservid, custid, screenid, seatno, reservdate) VALUES (NULL, 2, 1,  'A02', '2026-05-20');
INSERT INTO reservation (reservid, custid, screenid, seatno, reservdate) VALUES (NULL, 3, 3,  'B05', '2026-05-20');
INSERT INTO reservation (reservid, custid, screenid, seatno, reservdate) VALUES (NULL, 4, 5,  'C10', '2026-05-20');
INSERT INTO reservation (reservid, custid, screenid, seatno, reservdate) VALUES (NULL, 5, 7,  'A03', '2026-05-20');
INSERT INTO reservation (reservid, custid, screenid, seatno, reservdate) VALUES (NULL, 6, 9,  'D07', '2026-05-20');
INSERT INTO reservation (reservid, custid, screenid, seatno, reservdate) VALUES (NULL, 1, 10, 'B02', '2026-05-20');
INSERT INTO reservation (reservid, custid, screenid, seatno, reservdate) VALUES (NULL, 2, 6,  'A01', '2026-05-20');
