# CinemaSys 기능 명세 및 고도화 계획

> Java Swing, JDBC, MySQL 기반 영화 예매 관리 프로그램  
> 현재 구조를 유지하면서 도메인 규칙, 공통 유틸, 외부 API 연동을 단계적으로 고도화한다.

---

## 1. 목적

이 문서는 팀원들이 같은 기준으로 기능을 확장하기 위한 공유 문서이다.

- 현재 구현된 CRUD 기능과 레이어 구조를 정리한다.
- 도메인별로 추가할 비즈니스 규칙을 구체화한다.
- 공통 레이어에서 먼저 정리할 기반 작업을 분리한다.
- 영화진흥위원회 KOBIS OpenAPI를 어떤 기능에 활용할 수 있는지 정의한다.

---

## 2. 현재 프로젝트 구조

```text
src/main/java/
├── app/
│   ├── Main.java
│   └── MenuControl.java
├── common/
│   ├── DBUtil.java
│   ├── ComboItem.java
│   └── ui/
│       ├── DialogControl.java
│       └── WindowControl.java
├── domain/
│   ├── movie/
│   ├── theater/
│   ├── screening/
│   ├── customer/
│   └── reservation/
└── adapter/
    ├── movie/
    ├── theater/
    ├── screening/
    ├── customer/
    └── reservation/
```

### 레이어 역할

| 레이어 | 역할 | 주요 파일 |
|---|---|---|
| `app` | 애플리케이션 실행, 화면 조립, 메뉴 전환 | `Main.java`, `MenuControl.java` |
| `domain` | 엔티티, Repository 인터페이스, Service 비즈니스 규칙 | `*Service.java`, `*Repository.java`, 엔티티 |
| `adapter` | JDBC DAO 구현, Swing UI 이벤트 처리 | `*DAO.java`, `*Control.java`, `*ListPan.java` |
| `common` | DB 연결, 공통 UI, 검증, 설정, 외부 API 클라이언트 | `DBUtil.java`, `ComboItem.java` |

### 의존성 방향

```text
Main
  -> adapter/ui/*Control
    -> domain/*Service
      -> domain/*Repository
        <- adapter/*DAO
```

Spring 없이 수동으로 의존성을 조립한다. 따라서 새 기능을 추가할 때도 `Main.java`에서 어떤 Service와 DAO를 연결하는지 확인해야 한다.

---

## 3. 현재 기능 명세

### 공통 CRUD

현재 5개 도메인은 같은 CRUD 흐름을 가진다.

| 기능 | 설명 | 주요 위치 |
|---|---|---|
| 목록 조회 | DB 데이터를 JTable에 표시 | `*ListPan`, `*Service.findAll()` |
| 등록 | 입력 폼에서 값 입력 후 INSERT | `*InsFrm`, `*Service.save()` |
| 수정 | 선택 행 데이터를 수정 폼으로 전달 후 UPDATE | `*UpFrm`, `*Service.update()` |
| 삭제 | 선택 행 기준 DELETE | `*Control`, `*Service.delete()` |

### 도메인 관계

```text
Movie(영화)
  -> Screening(상영)

Theater(상영관)
  -> Screening(상영)

Customer(고객)
  -> Reservation(예약)

Screening(상영)
  -> Reservation(예약)
```

### 현재 주요 특징

- `screeningSelectAll`은 상영관 전체 좌석 수에서 예약 수를 빼서 `remain_seats`를 계산한다.
- `ScreeningService`는 영화/상영관 콤보박스 옵션을 제공한다.
- `ReservationService`는 고객/상영 콤보박스 옵션을 제공한다.
- 예약 등록 시 `reservdate`는 SQL에서 `CURDATE()`로 입력된다.

---

## 4. 팀 작업 방향

### 기본 원칙

- 팀원은 우선 `domain` 레이어의 규칙을 강화한다.
- DAO SQL 추가가 필요한 경우 `domain/*Repository.java`, `adapter/*DAO.java`, `sql.properties`를 함께 수정한다.
- UI는 도메인 규칙이 잡힌 뒤 최소한의 검증 메시지와 버튼 흐름만 붙인다.
- API Key, DB 비밀번호 같은 민감 정보는 문서와 코드에 직접 커밋하지 않는다.

### 권장 작업 순서

1. 도메인별 비즈니스 규칙 정의
2. Repository 인터페이스에 필요한 조회 메서드 추가
3. DAO와 `sql.properties` 구현
4. Service에서 규칙 검증
5. UI Control에서 예외 메시지 표시
6. 수동 테스트 시나리오 체크

---

## 5. 도메인별 고도화 명세

### 5-1. Reservation

예약은 프로그램의 핵심 기능이므로 가장 먼저 강화한다.

| 기능 | 설명 | 우선순위 |
|---|---|---|
| 동일 좌석 중복 예약 방지 | 같은 `screenid`와 `seatno` 조합은 한 번만 예약 가능 | 높음 |
| 잔여 좌석 초과 예약 방지 | `remain_seats <= 0`이면 예약 불가 | 높음 |
| 예약 취소 | 실제 DELETE 대신 상태값으로 취소 처리 검토 | 중간 |
| 좌석 번호 형식 검증 | `A1`, `B12` 같은 형식만 허용 | 중간 |

#### 필요한 변경 후보

```java
// ReservationRepository.java
boolean existsByScreenIdAndSeatNo(int screenId, String seatNo) throws SQLException;
int countByScreenId(int screenId) throws SQLException;
```

#### 완료 기준

- 이미 예약된 좌석을 다시 예약하면 저장되지 않는다.
- 잔여 좌석이 0인 상영에는 예약을 추가할 수 없다.
- UI에서 실패 이유가 사용자에게 표시된다.

---

### 5-2. Screening

상영은 영화, 상영관, 예약을 연결하는 중간 도메인이다.

| 기능 | 설명 | 우선순위 |
|---|---|---|
| 상영 시간 중복 방지 | 같은 상영관에서 같은 시간대 상영 등록 방지 | 높음 |
| 가격 검증 | 0원 이하 또는 비정상 가격 입력 방지 | 중간 |
| 지난 시간 상영 등록 방지 | 현재 시각보다 이전인 상영 일정 등록 방지 | 중간 |
| 상영 종료 시각 계산 | 영화 러닝타임이 추가될 경우 종료 시각 자동 계산 | 낮음 |

#### 필요한 변경 후보

```java
// ScreeningRepository.java
boolean existsByTheaterIdAndShowtime(int theaterId, LocalDateTime showtime) throws SQLException;
```

#### 완료 기준

- 같은 상영관, 같은 시간의 중복 상영 등록이 막힌다.
- 잘못된 가격 또는 날짜 입력 시 저장되지 않는다.

---

### 5-3. Customer

고객 정보는 예약과 직접 연결되므로 최소 검증을 추가한다.

| 기능 | 설명 | 우선순위 |
|---|---|---|
| 전화번호 형식 검증 | `010-0000-0000` 또는 숫자만 입력 방식 중 팀 기준 결정 | 중간 |
| 이메일 형식 검증 | `@`, 도메인 포함 여부 확인 | 낮음 |
| 중복 고객 방지 | 전화번호 기준 중복 등록 제한 검토 | 낮음 |

#### 완료 기준

- 빈 이름은 등록되지 않는다.
- 팀에서 정한 전화번호 형식이 아니면 저장되지 않는다.

---

### 5-4. Movie

KOBIS 연동의 중심 도메인이다.

| 기능 | 설명 | 우선순위 |
|---|---|---|
| 영화 기본값 검증 | 제목 필수, 관람 등급 선택값 제한 | 중간 |
| KOBIS 영화 매핑 | `movieCd` 또는 영화명 기준 외부 데이터와 연결 | 높음 |
| 박스오피스 순위 표시 | KOBIS 순위와 당일 관객 수를 영화 목록에 표시 | 중간 |

#### 확장 컬럼 후보

```sql
ALTER TABLE movie
    ADD COLUMN kobis_movie_cd VARCHAR(20) NULL,
    ADD COLUMN boxoffice_rank INT NULL,
    ADD COLUMN daily_audience_count INT NULL,
    ADD UNIQUE KEY uk_movie_kobis_movie_cd (kobis_movie_cd);
```

#### 완료 기준

- KOBIS에서 가져온 영화가 중복 저장되지 않는다.
- 영화 목록에서 박스오피스 순위와 관객 수를 확인할 수 있다.

---

### 5-5. Theater

상영관은 좌석 수 검증이 핵심이다.

| 기능 | 설명 | 우선순위 |
|---|---|---|
| 좌석 수 검증 | 1석 이상만 허용 | 중간 |
| 상영관명 중복 방지 | 같은 이름의 상영관 중복 등록 제한 검토 | 낮음 |
| 예약된 상영관 삭제 방지 | 연결된 상영/예약이 있으면 삭제 불가 메시지 표시 | 중간 |

---

## 6. Common / 인프라 고도화 계획

본인 담당으로 먼저 확인하면 좋은 영역이다.

### 6-1. DBUtil 개선

현재 `DBUtil`은 하나의 static connection을 재사용한다. 소규모 Swing 프로젝트에서는 동작하지만, 에러 메시지와 설정 검증이 약하다.

| 개선 항목 | 설명 | 우선순위 |
|---|---|---|
| 설정 파일 누락 처리 | `db.properties`, `sql.properties`가 없을 때 명확한 예외 발생 | 높음 |
| SQL key 누락 처리 | `getSQL(key)`가 null이면 어떤 key가 없는지 표시 | 높음 |
| 연결 재생성 | connection이 닫혔으면 다시 연결 | 중간 |
| 민감 정보 분리 | DB 비밀번호, API Key는 `.gitignore` 대상 파일 또는 환경 변수 사용 | 높음 |

#### 개선 방향

```java
public static String getSQL(String key) {
    String sql = sqlProperties.getProperty(key);
    if (sql == null || sql.isBlank()) {
        throw new IllegalArgumentException("SQL key not found: " + key);
    }
    return sql;
}
```

---

### 6-2. 공통 검증 유틸

도메인별 UI에서 검증 로직이 중복되지 않도록 공통 유틸을 둘 수 있다.

```text
common/
├── Validator.java
├── DateUtil.java
├── ConfigUtil.java
├── KobisApiClient.java
└── KobisMovieDto.java
```

| 파일 | 역할 |
|---|---|
| `Validator.java` | 빈 값, 숫자 범위, 전화번호, 이메일, 좌석번호 검증 |
| `DateUtil.java` | `LocalDateTime` 파싱/포맷 변환 |
| `ConfigUtil.java` | 환경 변수 또는 properties 파일에서 설정 읽기 |
| `KobisApiClient.java` | KOBIS API 요청 담당 |
| `KobisMovieDto.java` | KOBIS 응답 데이터를 내부에서 쓰기 쉽게 보관 |

---

### 6-3. UI 공통 개선

| 개선 항목 | 설명 |
|---|---|
| 선택 행 없음 처리 | 삭제/수정 버튼 클릭 시 선택 행이 없으면 안내 메시지 |
| 입력 실패 메시지 통일 | `DialogControl`에서 성공/경고/에러 메시지 공통 처리 |
| 목록 새로고침 통일 | 등록/수정/삭제 후 JTable 갱신 흐름 통일 |
| 콤보박스 기본 선택값 | 선택하지 않은 상태를 명확히 표현 |

---

## 7. KOBIS OpenAPI 활용 계획

### 7-1. 활용 가능성

영화진흥위원회 KOBIS OpenAPI는 현재 프로젝트에서 다음 방식으로 활용할 수 있다.

| 활용안 | 설명 | 난이도 | 추천 |
|---|---|---|---|
| 박스오피스 TOP 10 조회 | 전날 일별 박스오피스 순위를 가져와 화면에 표시 | 낮음 | 1차 구현 추천 |
| 영화 자동 등록 | 박스오피스 영화 중 DB에 없는 영화를 자동 등록 | 중간 | 2차 구현 추천 |
| 영화 목록 순위 컬럼 | 기존 영화와 KOBIS 데이터를 매칭해 순위/관객 수 표시 | 중간 | 2차 구현 추천 |
| 상영 일정 추천 | 인기 영화 기반으로 상영 등록을 추천 | 높음 | 시간이 남으면 진행 |

### 7-2. API 정보

| 항목 | 내용 |
|---|---|
| 제공 기관 | 영화진흥위원회 KOBIS |
| API | 일별 박스오피스 조회 |
| 엔드포인트 | `http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json` |
| 필수 파라미터 | `key`, `targetDt` |
| 날짜 형식 | `YYYYMMDD` |
| 조회 기준 | 보통 당일이 아닌 전날 날짜 조회 |

API Key는 코드와 문서에 직접 저장하지 않는다. 발급받은 키는 로컬 환경 변수 또는 git에 포함되지 않는 properties 파일에 둔다.

```bash
export KOBIS_API_KEY=발급받은_키
```

또는:

```properties
# src/main/resources/kobis.properties
kobis.api.key=발급받은_키
```

`kobis.properties`는 `.gitignore`에 추가하고, 예시 파일만 커밋한다.

```text
src/main/resources/kobis.properties
src/main/resources/kobis.properties.example
```

### 7-3. 응답 데이터 예시

```json
{
  "boxOfficeResult": {
    "dailyBoxOfficeList": [
      {
        "rank": "1",
        "movieCd": "20250001",
        "movieNm": "마이클",
        "audiCnt": "179024"
      }
    ]
  }
}
```

프로젝트에서는 우선 아래 값만 사용한다.

| KOBIS 필드 | 내부 의미 |
|---|---|
| `rank` | 박스오피스 순위 |
| `movieCd` | KOBIS 영화 코드, 중복 import 방지 기준 |
| `movieNm` | 영화명 |
| `audiCnt` | 당일 관객 수 |

### 7-4. 1차 구현 범위

가장 현실적인 1차 목표는 "박스오피스 조회 후 영화 목록에 반영할 준비"까지이다.

```text
[메뉴] 박스오피스 불러오기
  -> KOBIS API 호출
  -> 응답 파싱
  -> JTable 또는 콘솔에 TOP 10 표시
  -> 이후 Movie 저장 기능과 연결
```

#### 1차 완료 기준

- API Key를 환경 변수 또는 `kobis.properties`에서 읽는다.
- 전날 날짜를 자동 계산한다.
- TOP 10 영화명, 순위, 관객 수를 가져온다.
- API 오류가 발생해도 프로그램 전체가 종료되지 않는다.

### 7-5. 2차 구현 범위

2차 목표는 KOBIS 데이터를 실제 `movie` 테이블과 연결하는 것이다.

```text
KOBIS TOP 10 조회
  -> movieCd 기준으로 이미 등록된 영화인지 확인
  -> 없으면 Movie로 변환
  -> genre/director/rating은 기본값 또는 수동 보완 대상으로 저장
  -> MovieListPan 새로고침
```

#### Repository 변경 후보

```java
// MovieRepository.java
boolean existsByKobisMovieCd(String kobisMovieCd) throws SQLException;
void saveFromKobis(Movie movie) throws SQLException;
```

#### Service 변경 후보

```java
// MovieService.java
public int importFromBoxOffice(List<KobisMovieDto> movies) throws SQLException {
    int inserted = 0;
    for (KobisMovieDto dto : movies) {
        if (!repository.existsByKobisMovieCd(dto.getMovieCd())) {
            repository.saveFromKobis(dto.toMovie());
            inserted++;
        }
    }
    return inserted;
}
```

### 7-6. 주의점

- KOBIS 일별 박스오피스는 보통 전날 날짜로 조회해야 안정적으로 나온다.
- API Key를 `DOCS.md`, Java 소스, Git 커밋에 직접 남기지 않는다.
- 현재 프로젝트에는 JSON 라이브러리가 별도로 보이지 않는다. `Gson`, `org.json`, `Jackson` 중 하나를 추가하거나, 팀 범위가 작으면 최소 파싱 방식으로 시작한다.
- `movieNm`만으로 중복 판단하면 동명이인 영화 문제가 생길 수 있으므로 `movieCd`를 저장하는 편이 낫다.

---

## 8. DB 변경 후보

KOBIS까지 연결하려면 `movie` 테이블 확장이 필요하다.

```sql
ALTER TABLE movie
    ADD COLUMN kobis_movie_cd VARCHAR(20) NULL,
    ADD COLUMN boxoffice_rank INT NULL,
    ADD COLUMN daily_audience_count INT NULL;

CREATE UNIQUE INDEX uk_movie_kobis_movie_cd
    ON movie(kobis_movie_cd);
```

예약 중복 방지는 DB 제약으로도 보강할 수 있다.

```sql
ALTER TABLE reservation
    ADD CONSTRAINT uk_reservation_screen_seat
    UNIQUE (screenid, seatno);
```

도메인 Service에서 먼저 검증하고, DB unique 제약은 마지막 방어선으로 둔다.

---

## 9. 작업 우선순위

### 1순위

- `DBUtil.getSQL()` null 방어
- `db.properties`, `kobis.properties` 예시 파일 정리
- 예약 중복 좌석 방지
- 잔여 좌석 초과 예약 방지

### 2순위

- 상영 시간 중복 방지
- 입력값 검증 공통화
- KOBIS TOP 10 조회 기능
- KOBIS DTO와 Client 작성

### 3순위

- KOBIS 영화 자동 등록
- 영화 목록에 박스오피스 순위 표시
- 예약 취소 상태값 도입
- 상영 일정 추천 기능

---

## 10. 수동 테스트 체크리스트

### Reservation

- [ ] 같은 상영, 같은 좌석으로 두 번 예약하면 실패한다.
- [ ] 잔여 좌석이 0이면 예약 등록이 실패한다.
- [ ] 고객 또는 상영을 선택하지 않으면 저장되지 않는다.
- [ ] 삭제 후 상영 목록의 잔여 좌석이 다시 반영된다.

### Screening

- [ ] 같은 상영관 같은 시간으로 등록하면 실패한다.
- [ ] 가격이 0 이하이면 저장되지 않는다.
- [ ] 영화 또는 상영관을 선택하지 않으면 저장되지 않는다.

### KOBIS

- [ ] `KOBIS_API_KEY`가 없으면 명확한 안내 메시지가 나온다.
- [ ] 전날 날짜 기준 TOP 10을 조회한다.
- [ ] 네트워크 오류 또는 API 오류가 발생해도 앱이 종료되지 않는다.
- [ ] 같은 `movieCd` 영화는 중복 등록되지 않는다.

---

## 11. 환경 설정

### DB 설정

`src/main/resources/db.properties`는 로컬에서만 생성한다.

```properties
url=jdbc:mysql://localhost:3306/cinemasys
user=root
password=your_password
```

예시 파일은 `src/main/resources/db.properties.example`로 관리한다.

### KOBIS 설정

권장 방식은 환경 변수이다.

```bash
export KOBIS_API_KEY=your_api_key
make run
```

properties 파일을 사용할 경우:

```properties
kobis.api.key=your_api_key
```

`src/main/resources/kobis.properties`는 커밋하지 않는다.

### 실행

#### Gradle Wrapper (권장 — 모든 환경 공통)

Gradle이 설치되어 있지 않아도 Wrapper가 자동으로 다운받아 실행한다.

| 환경 | 명령어 |
|---|---|
| Mac / Linux 터미널 | `./gradlew run` |
| Windows CMD / PowerShell | `gradlew.bat run` |

```bash
# Mac / Linux
./gradlew run

# Windows
gradlew.bat run
```

#### IDE별 실행

| IDE | 방법 |
|---|---|
| **VS Code** | 실행 버튼 (▷) 클릭 — `.vscode/launch.json` 설정 포함 |
| **IntelliJ** | `build.gradle` 열기 → Gradle 프로젝트 자동 인식 → 실행 버튼 |
| **Eclipse** | File → Import → Gradle → Existing Gradle Project → 실행 버튼 |

#### Makefile (Mac / Linux 터미널 전용)

```bash
make build
make run
make clean
```

---

## 12. 팀 공유 메모

- 도메인 작업자는 `domain/*Service.java`에 규칙을 먼저 넣고, 필요한 조회 기능을 Repository 인터페이스에 추가한다.
- DAO 작업자는 새 Repository 메서드에 맞춰 `adapter/*DAO.java`와 `sql.properties`를 함께 수정한다.
- 공통 작업자는 설정, 검증, 예외 메시지, KOBIS API Client를 먼저 안정화한다.
- UI 작업자는 Service에서 던진 예외를 사용자 메시지로 보여주는 흐름을 맞춘다.
