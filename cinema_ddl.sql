CREATE DATABASE IF NOT EXISTS cinemasys DEFAULT CHARACTER SET utf8mb4;
USE cinemasys;

-- 테이블 초기화 (FK 역순)
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS screening;
DROP TABLE IF EXISTS theater;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS movie;

-- 영화
CREATE TABLE movie (
    movieid  INT AUTO_INCREMENT PRIMARY KEY,
    title    VARCHAR(200) NOT NULL,
    genre    VARCHAR(100),
    director VARCHAR(100),
    rating   VARCHAR(50),

    UNIQUE(title)
);

-- 상영관
CREATE TABLE theater (
    theaterid   INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    total_seats INT NOT NULL,

    UNIQUE(name)
);

-- 상영일정
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

-- 고객
CREATE TABLE customer (
    custid INT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(50) NOT NULL,
    phone  VARCHAR(20) NOT NULL,
    email  VARCHAR(100),

    UNIQUE(phone)
);

-- 예약
CREATE TABLE reservation (
    reservid   INT AUTO_INCREMENT PRIMARY KEY,
    custid     INT NOT NULL,
    screenid   INT NOT NULL,
    seatno     VARCHAR(10) NOT NULL,
    reservdate DATE NOT NULL,

    UNIQUE(screenid, seatno),

    FOREIGN KEY (custid)   REFERENCES customer(custid),
    FOREIGN KEY (screenid) REFERENCES screening(screenid)
);

-- 상영관 초기 데이터
INSERT INTO theater VALUES (NULL, '1관',    100);
INSERT INTO theater VALUES (NULL, '2관',     80);
INSERT INTO theater VALUES (NULL, 'VIP관',   30);
INSERT INTO theater VALUES (NULL, 'IMAX관', 200);
INSERT INTO theater VALUES (NULL, '4DX관',   80);

-- 영화 샘플 데이터 (앱 최초 실행 시 KOBIS API에서 자동 삽입 가능)
INSERT INTO movie VALUES (NULL, '인터스텔라', 'SF',   '크리스토퍼 놀란', 12);
INSERT INTO movie VALUES (NULL, '기생충',     '드라마','봉준호',         15);
INSERT INTO movie VALUES (NULL, '어벤져스',   '액션', '루소 형제',       12);

-- 고객 초기 데이터
INSERT INTO customer VALUES (NULL, '김가원', '010-1234-5678', 'gawon@email.com');
INSERT INTO customer VALUES (NULL, '이민준', '010-2345-6789', 'minjun@email.com');
INSERT INTO customer VALUES (NULL, '박지수', '010-3456-7890', 'jisu@email.com');
INSERT INTO customer VALUES (NULL, '김철수', '010-1111-2222', 'chulsoo@email.com');
INSERT INTO customer VALUES (NULL, '이영희', '010-3333-4444', 'younghee@email.com');
INSERT INTO customer VALUES (NULL, '박민준', '010-5555-6666', 'pminjun@email.com');

-- 상영일정
INSERT INTO screening VALUES (NULL, 1, 1, '2026-05-20 14:00:00', 15000);
INSERT INTO screening VALUES (NULL, 1, 2, '2026-05-20 18:00:00', 15000);
INSERT INTO screening VALUES (NULL, 2, 1, '2026-05-21 11:00:00', 12000);
INSERT INTO screening VALUES (NULL, 3, 3, '2026-05-21 20:00:00', 20000);

-- 예약 샘플 데이터
INSERT INTO reservation VALUES (NULL, 1, 1, 'A1', CURDATE());
INSERT INTO reservation VALUES (NULL, 2, 1, 'A2', CURDATE());
INSERT INTO reservation VALUES (NULL, 3, 2, 'B1', CURDATE());
INSERT INTO reservation VALUES (NULL, 1, 3, 'C1', CURDATE());
INSERT INTO reservation VALUES (NULL, 2, 4, 'D1', CURDATE());
