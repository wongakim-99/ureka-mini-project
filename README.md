# 영화 예약 관리 시스템

Java 21, Swing, JDBC, MySQL 기반 영화 예약 관리 프로그램입니다.

---

## 기술 스택

- Java 21
- Java Swing
- JDBC
- MySQL
- Gradle
- Gson

---

## 프로젝트 구조

```text
ureka-mini-project/
├── src/main/java/
│   ├── Main.java
│   ├── application/              # 객체 조립
│   ├── domain/                   # 엔티티, Service, Repository 인터페이스
│   ├── infrastructure/           # DB, KOBIS, JDBC Repository 구현
│   └── presentation/swing/       # Swing 화면, Controller, Frame, Panel
├── src/main/resources/
│   ├── schema.sql
│   ├── sql.properties
│   └── db.properties             # 로컬 설정, git 제외
├── lib/
├── build.gradle
└── Makefile
```

주요 의존 방향:

```text
Main
  -> presentation.swing.MainDashboard
    -> application.AppFactory
      -> domain
      -> infrastructure
```

---

## 시작하기 전에

### 1. MySQL DB 준비

로컬 MySQL에 `cinemasys` 데이터베이스가 필요합니다. 수동으로 초기화하려면:

```bash
mysql -u root -p < cinema_ddl.sql
```

앱 실행 시 `src/main/resources/db.properties`의 `ddl.auto` 값에 따라 `schema.sql` 기반 초기화도 수행됩니다.

### 2. db.properties 생성

```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

`src/main/resources/db.properties`를 본인 환경에 맞게 수정합니다.

```properties
url=jdbc:mysql://localhost:3306/cinemasys?useUnicode=true&characterEncoding=UTF-8
user=root
password=your_password
kobis.api.key=your_kobis_api_key
ddl.auto=none
```

`db.properties`는 로컬 설정 파일이며 Git에 커밋하지 않습니다.

---

## 실행 방법

### Gradle

```bash
./gradlew run
```

빌드만 확인하려면:

```bash
./gradlew build
```

### Makefile

```bash
make run
```

| 명령어 | 설명 |
|---|---|
| `make build` | `bin/`에 직접 컴파일 |
| `make run` | 컴파일 후 실행 |
| `make clean` | `bin/` 정리 |

---

## IDE 실행

### Eclipse

1. `File > Import > General > Existing Projects into Workspace`
2. 프로젝트 폴더 선택
3. `src/main/java/Main.java` 실행

### VS Code

1. Extension Pack for Java 설치
2. 프로젝트 폴더 열기
3. `src/main/java/Main.java` 실행

---

## 주의 사항

- `src/main/resources/db.properties`는 Git 제외 대상입니다.
- `.idea/`, `.vscode/`는 Git 제외 대상입니다.
- DB가 실행 중인 상태에서 프로그램을 실행해야 합니다.
- KOBIS API Key는 문서나 Java 소스에 직접 커밋하지 않습니다.
