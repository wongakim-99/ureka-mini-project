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
    UNIQUE(phone)
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

INSERT INTO customer VALUES (NULL, '김가원', '010-1234-5678', 'gawon@email.com');
INSERT INTO customer VALUES (NULL, '이민준', '010-2345-6789', 'minjun@email.com');
INSERT INTO customer VALUES (NULL, '박지수', '010-3456-7890', 'jisu@email.com');
INSERT INTO customer VALUES (NULL, '김철수', '010-1111-2222', 'chulsoo@email.com');
INSERT INTO customer VALUES (NULL, '이영희', '010-3333-4444', 'younghee@email.com');
INSERT INTO customer VALUES (NULL, '박민준', '010-5555-6666', 'pminjun@email.com');
