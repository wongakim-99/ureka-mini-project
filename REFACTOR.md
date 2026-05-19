# Refactor Plan

> 목표: 현재 기능을 유지하면서 패키지 책임을 명확히 재정렬한다.  
> 기준: DDD의 `domain` 순수성은 유지하고, Swing UI와 JDBC 구현은 도메인 밖으로 분리한다.

---

## 1. 현재 구조 검토

현재 소스 구조는 크게 `app`, `domain`, `adapter`, `common`으로 나뉜다.

```text
src/main/java/
├── app/
│   ├── Main.java
│   ├── MainDashboard.java
│   └── MenuControl.java
├── adapter/
│   ├── customer/
│   │   ├── CustDAO.java
│   │   └── ui/
│   ├── movie/
│   │   ├── MovieDAO.java
│   │   └── ui/
│   ├── reservation/
│   │   ├── ReservDAO.java
│   │   └── ui/
│   ├── revenue/
│   │   ├── RevenueDAO.java
│   │   └── ui/
│   ├── screening/
│   │   ├── ScreeningDAO.java
│   │   └── ui/
│   └── theater/
│       ├── TheaterDAO.java
│       └── ui/
├── common/
│   ├── DBUtil.java
│   ├── SchemaManager.java
│   ├── KobisImporter.java
│   ├── ComboItem.java
│   └── ui/
└── domain/
    ├── customer/
    ├── movie/
    ├── reservation/
    ├── revenue/
    ├── screening/
    └── theater/
```

현재 의존 흐름은 대략 다음과 같다.

```text
app.Main
  -> adapter.*.ui.*Control
    -> domain.*.*Service
      -> domain.*.*Repository
        <- adapter.*.*DAO
```

겉보기에는 `adapter`가 입출력 계층처럼 보이지만, 실제로는 JDBC DAO와 Swing UI가 함께 들어가 있다. 이 때문에 `adapter`의 의미가 넓어졌고, 어디에 무엇을 둬야 하는지 판단하기 어려워졌다.

---

## 2. 확인된 문제

### 2-1. `app` 패키지 역할이 애매함

- `Main.java`는 실행 진입점이면서 Swing 대시보드 전체를 직접 구현한다.
- `MainDashboard.java`도 거의 같은 대시보드 구현을 갖고 있어 중복/구버전 파일로 보인다.
- `MenuControl.java`는 현재 `Main.java`의 메뉴 전환 방식과 분리되어 있고, 실제 주 화면에서 핵심으로 쓰이는 구조가 아니다.

### 2-2. `adapter`에 서로 다른 책임이 섞임

`adapter/movie/MovieDAO.java`와 `adapter/movie/ui/MovieControl.java`는 모두 adapter 아래에 있지만 책임이 다르다.

- DAO: DB/JDBC 영속성 구현
- UI Control/Form/Panel: Swing 프레젠테이션 구현

둘 다 외부 입출력이라는 넓은 의미의 adapter는 맞지만, 현재 프로젝트에서는 패키지명이 너무 포괄적이라 구조 이해를 방해한다.

### 2-3. UI가 DAO 구현체를 직접 생성함

예:

```java
service = new MovieService(new MovieDAO());
```

이 패턴이 각 `*Control`에 반복된다. UI 계층이 JDBC 구현체를 직접 알고 있으므로, 의존성 조립 위치가 분산되어 있다.

### 2-4. `domain`이 UI 자료형을 일부 알고 있음

`ReservationRepository`, `ScreeningRepository`가 `common.ComboItem`을 반환한다.

```java
List<ComboItem> findCustomerOptions() throws SQLException;
```

`ComboItem`은 Swing 콤보박스 표시를 위한 성격이 강하다. DDD 관점에서는 도메인이 UI 표시 모델을 직접 의존하지 않는 편이 좋다.

### 2-5. `common`이 공통이라는 이유로 너무 많은 책임을 가짐

- `DBUtil`: DB 연결, SQL properties
- `SchemaManager`: 스키마 초기화
- `KobisImporter`: 외부 API 연동과 DB 저장
- `common.ui`: Swing 공통 UI
- `ComboItem`: UI 옵션 모델

기능이 커질수록 `common`은 사실상 두 번째 잡동사니 패키지가 될 가능성이 높다.

---

## 3. 방향 결정

### 결론: `domain` 안에 `ui`를 넣지 않는다

`domain/movie/ui`, `domain/reservation/ui`처럼 도메인 내부에 Swing 파일을 넣으면 도메인 패키지가 UI 프레임워크를 알게 된다. 이는 DDD 기준으로는 오히려 경계가 흐려진다.

권장 방향은 다음과 같다.

- `domain`: 엔티티, 도메인 서비스, Repository 인터페이스만 둔다.
- `presentation.swing`: Swing 화면, 컨트롤러, 폼, 패널을 둔다.
- `infrastructure.persistence`: JDBC DAO 구현체를 둔다.
- `application`: 유스케이스 또는 의존성 조립을 둔다.
- `Main.java`: 실행 진입점만 담당한다.

---

## 4. 목표 구조

최종 목표 구조는 다음과 같이 잡는다.

```text
src/main/java/
├── Main.java
├── application/
│   └── AppFactory.java
├── domain/
│   ├── common/
│   │   └── OptionItem.java
│   ├── customer/
│   │   ├── Customer.java
│   │   ├── CustomerRepository.java
│   │   └── CustomerService.java
│   ├── movie/
│   │   ├── Movie.java
│   │   ├── MovieRepository.java
│   │   └── MovieService.java
│   ├── reservation/
│   ├── revenue/
│   ├── screening/
│   └── theater/
├── infrastructure/
│   ├── db/
│   │   ├── DBUtil.java
│   │   └── SchemaManager.java
│   ├── kobis/
│   │   └── KobisImporter.java
│   └── persistence/
│       ├── customer/
│       │   └── CustomerJdbcRepository.java
│       ├── movie/
│       │   └── MovieJdbcRepository.java
│       ├── reservation/
│       │   └── ReservationJdbcRepository.java
│       ├── revenue/
│       │   └── RevenueJdbcRepository.java
│       ├── screening/
│       │   └── ScreeningJdbcRepository.java
│       └── theater/
│           └── TheaterJdbcRepository.java
└── presentation/
    └── swing/
        ├── MainDashboard.java
        ├── menu/
        │   └── MenuController.java
        ├── common/
        │   ├── DatePickerDialog.java
        │   ├── DialogControl.java
        │   └── WindowControl.java
        ├── customer/
        ├── movie/
        ├── reservation/
        ├── revenue/
        ├── screening/
        └── theater/
```

### `Main.java`

`src/main/java/Main.java`에 둔다. 단, 기본 패키지 클래스는 다른 패키지에서 import하기 어렵기 때문에 이 파일은 아주 얇은 bootstrap 역할만 맡긴다.

```java
public class Main {
    public static void main(String[] args) {
        application.AppFactory.start();
    }
}
```

### `presentation.swing.MainDashboard`

현재 `app.Main`에 들어 있는 Swing 대시보드 구현은 여기로 옮긴다. 진입점과 화면 구현을 분리한다.

### `application.AppFactory`

DAO, Service, Controller 생성 책임을 한곳에 모은다. 지금처럼 각 UI 컨트롤러가 직접 `new MovieDAO()`를 하지 않도록 한다.

### `infrastructure.persistence.*`

현재 `adapter/*DAO.java`는 이쪽으로 이동하고 이름을 Repository 구현체답게 바꾼다.

| 현재 | 변경 후보 |
|---|---|
| `adapter.movie.MovieDAO` | `infrastructure.persistence.movie.MovieJdbcRepository` |
| `adapter.customer.CustDAO` | `infrastructure.persistence.customer.CustomerJdbcRepository` |
| `adapter.reservation.ReservDAO` | `infrastructure.persistence.reservation.ReservationJdbcRepository` |
| `adapter.screening.ScreeningDAO` | `infrastructure.persistence.screening.ScreeningJdbcRepository` |
| `adapter.theater.TheaterDAO` | `infrastructure.persistence.theater.TheaterJdbcRepository` |
| `adapter.revenue.RevenueDAO` | `infrastructure.persistence.revenue.RevenueJdbcRepository` |

### `presentation.swing.*`

현재 `adapter/*/ui`에 있는 파일은 도메인별로 이쪽으로 이동한다.

| 현재 | 변경 후보 |
|---|---|
| `adapter.movie.ui.*` | `presentation.swing.movie.*` |
| `adapter.customer.ui.*` | `presentation.swing.customer.*` |
| `adapter.reservation.ui.*` | `presentation.swing.reservation.*` |
| `adapter.screening.ui.*` | `presentation.swing.screening.*` |
| `adapter.theater.ui.*` | `presentation.swing.theater.*` |
| `adapter.revenue.ui.*` | `presentation.swing.revenue.*` |
| `common.ui.*` | `presentation.swing.common.*` |

---

## 5. 리팩토링 순서

기능 유지가 가장 중요하므로 파일 이동을 한 번에 크게 하지 않는다. 컴파일 가능한 작은 단위로 진행한다.

### Step 1. 진입점과 대시보드 분리 - 완료

- `src/main/java/Main.java`를 새로 만들었다.
- 현재 `app.Main`의 화면 구현은 `presentation.swing.MainDashboard`로 이동했다.
- `build.gradle`의 `application.mainClass`를 `Main`으로 변경했다.
- 구버전 중복 파일이던 `app.MainDashboard`는 제거했다.
- 기존 `app.Main`도 제거하고 최상위 `Main`에서 `MainDashboard.launch()`만 호출하도록 정리했다.
- `app.MenuControl`은 다음 UI 패키지 이동 단계에서 실제 사용 여부를 확인한 뒤 이동하거나 제거하기로 남겨두었다.

완료 기준:

- `./gradlew build` 성공
- 앱 실행 시 기존 화면과 초기 데이터 동기화 동작 유지

### Step 2. Swing UI 패키지 이동 - 완료

- `adapter/*/ui`를 `presentation.swing/*`로 이동했다.
- `common.ui`를 `presentation.swing.common`으로 이동했다.
- 사용 여부가 애매했던 `app.MenuControl`은 삭제하지 않고 `presentation.swing.menu.MenuControl`로 이동했다.
- package/import만 변경하고 동작 로직은 건드리지 않았다.

완료 기준:

- 모든 CRUD 화면이 기존과 동일하게 열린다.
- 목록/검색/추가/수정/삭제 버튼 동작 유지
- `./gradlew build` 성공

### Step 3. JDBC DAO 패키지 이동 - 완료

- `adapter/*DAO.java`를 `infrastructure.persistence/*/*JdbcRepository.java`로 이동했다.
- 클래스명도 `DAO`보다 `JdbcRepository` 기준으로 정리했다.
- `common.DBUtil` import는 임시로 유지했다.
- Swing Controller의 import와 생성 코드는 새 `JdbcRepository` 클래스명으로 갱신했다.
- UI가 `infrastructure.persistence`를 직접 import하는 상태는 Step 4에서 `AppFactory`로 의존성 조립을 모으며 제거한다.

완료 기준:

- 기존 `domain.*Repository` 인터페이스 구현 유지
- 모든 DB CRUD 동작 유지
- `./gradlew build` 성공

### Step 4. 의존성 조립 위치 통합 - 완료

- 각 Swing Control 생성자에서 Service를 직접 만들지 않도록 변경했다.
- `MovieControl(MovieService service, JDialog dialog, JLabel dialogLabel)`처럼 Service를 주입받게 했다.
- `application.AppFactory`에서 Repository -> Service -> Control을 조립한다.
- `MainDashboard`는 직접 Controller를 생성하지 않고 `AppFactory`의 생성 메서드를 호출한다.

완료 기준:

- UI 계층이 `infrastructure.persistence`를 import하지 않는다.
- Repository 구현체 생성 위치가 `AppFactory` 하나로 모인다.
- `./gradlew build` 성공

### Step 5. `common` 해체 - 완료

- `DBUtil`, `SchemaManager` -> `infrastructure.db`
- `KobisImporter` -> `infrastructure.kobis`
- `ComboItem` -> `domain.common.OptionItem`

주의:

- `ComboItem`은 Repository 인터페이스에 노출되어 있으므로 UI 패키지로 옮기지 않았다.
- `application.dto`에 두면 domain이 application을 의존하게 되므로, Repository 계약에 쓰이는 UI 중립 값 객체로 보고 `domain.common.OptionItem`에 배치했다.

완료 기준:

- `domain` 패키지가 `common` 또는 `presentation`을 import하지 않는다.
- `common` 패키지는 제거되었다.
- `./gradlew build` 성공

### Step 6. 프레젠테이션 잔여 정리 - 완료

- `presentation.swing.menu.MenuControl`을 목표 구조의 이름에 맞춰 `presentation.swing.menu.MenuController`로 변경했다.
- `MainDashboard`에서 사용하지 않는 `DialogControl` import를 제거했다.

완료 기준:

- `./gradlew build` 성공

### Step 7. Swing 클래스명 정리 - 완료

- `*Control`을 `*Controller`로 변경했다.
- `*InsFrm`을 `*CreateFrame`으로 변경했다.
- `*UpFrm`을 `*UpdateFrame`으로 변경했다.
- `*ListPan`을 `*ListPanel`로 변경했다.
- `Cust*`, `Reserv*` 축약명을 각각 `Customer*`, `Reservation*`으로 변경했다.
- `AppFactory`, `MainDashboard`, 각 Frame/Controller의 타입과 메서드 참조를 새 이름 기준으로 갱신했다.
- 내부 변수명도 `movieCreateFrame`, `reservationController`, `customerList`처럼 새 클래스명과 맞춰 정리했다.

완료 기준:

- `./gradlew build` 성공

---

## 6. 의존성 원칙

리팩토링 후 지켜야 할 의존 방향은 다음과 같다.

```text
Main
  -> application
    -> presentation.swing
    -> infrastructure
    -> domain

presentation.swing
  -> application
  -> domain

infrastructure
  -> domain

domain
  -> Java standard library only
```

금지할 의존 방향:

```text
domain -> presentation.swing
domain -> infrastructure
domain -> common.ui
presentation.swing -> infrastructure.persistence
```

---

## 7. 네이밍 규칙

현재 약어와 역할명이 섞여 있으므로 이동 시 같이 정리한다.

| 현재 | 변경 후보 |
|---|---|
| `Cust*` | `Customer*` |
| `Reserv*` | `Reservation*` |
| `*DAO` | `*JdbcRepository` |
| `*Control` | `*Controller` |
| `*InsFrm` | `*CreateFrame` |
| `*UpFrm` | `*UpdateFrame` |
| `*ListPan` | `*ListPanel` |

단, 이름 변경은 import 변경량이 커지므로 패키지 이동과 동시에 모두 처리하지 않는다. 먼저 패키지 이동, 그 다음 클래스명 정리를 권장한다.

---

## 8. 보존해야 할 현재 기능

리팩토링 중 다음 기능은 반드시 유지한다.

- 영화, 예약, 상영 일정, 고객, 수입 목록 조회
- 검색 기능
- 추가/수정/삭제 기능
- 체크박스 선택 기반 삭제/수정 버튼 활성화
- 수입 관리 화면의 총 수입 표시
- KOBIS 데이터 동기화 버튼
- 앱 시작 시 스키마 초기화
- 영화 테이블이 비어 있을 때 KOBIS 초기 데이터 로드
- `db.properties`, `sql.properties`, `schema.sql` 기반 실행 방식

---

## 9. 1차 작업 범위 제안

첫 리팩토링 PR 또는 커밋은 다음 범위로 제한한다.

1. `Main.java`를 `src/main/java` 바로 아래로 이동
2. `app.Main`의 화면 구현을 `presentation.swing.MainDashboard`로 이동
3. `build.gradle`의 mainClass 변경
4. 중복 `app.MainDashboard` 정리
5. 동작 확인

이 단계에서는 DAO/UI 패키지 이동을 하지 않는다. 진입점과 화면 구현 분리만 먼저 끝내면 이후 이동 작업의 위험이 줄어든다.

---

## 10. 검증 명령

리팩토링 단계마다 최소한 아래 명령을 실행한다.

```bash
./gradlew clean build
```

GUI 실행 확인:

```bash
./gradlew run
```

수동 테스트:

- 앱 실행
- 영화 관리 기본 목록 표시
- 검색어 입력 후 검색
- 영화 추가/수정/삭제
- 예약 추가/수정/삭제
- 상영 일정 추가/수정/삭제
- 고객 추가/수정/삭제
- 수입 관리 조회
- 데이터 동기화 버튼 클릭
