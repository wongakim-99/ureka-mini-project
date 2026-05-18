CREATE DATABASE IF NOT EXISTS cinemasys DEFAULT CHARACTER SET utf8mb4;
USE cinemasys;

-- 테이블 초기화 (FK 역순)
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS screening;
DROP TABLE IF EXISTS theater;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS movie;

-- 테이블 생성
CREATE TABLE movie (
    movieid  INT AUTO_INCREMENT PRIMARY KEY,
    title    VARCHAR(100) NOT NULL,
    genre    VARCHAR(50),
    director VARCHAR(50),
    rating   VARCHAR(20)
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
    email  VARCHAR(100)
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

-- 상영관 초기 데이터
INSERT INTO theater VALUES (NULL, '1관',    100);
INSERT INTO theater VALUES (NULL, '2관',     80);
INSERT INTO theater VALUES (NULL, 'VIP관',   30);
INSERT INTO theater VALUES (NULL, 'IMAX관', 200);
INSERT INTO theater VALUES (NULL, '4DX관',   80);

-- 고객 초기 데이터
INSERT INTO customer VALUES (NULL, '김가원', '010-1234-5678', 'kim@email.com');
INSERT INTO customer VALUES (NULL, '이민준', '010-2345-6789', 'lee@email.com');
INSERT INTO customer VALUES (NULL, '박지수', '010-3456-7890', 'park@email.com');

-- 영화: 앱 최초 실행 시 KOBIS 박스오피스 API에서 자동 삽입
-- 상영일정: 관리자가 앱에서 직접 등록
