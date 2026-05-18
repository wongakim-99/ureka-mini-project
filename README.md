# 영화 예약 관리 시스템

Java + JDBC + MySQL + Swing 기반 영화 예약 CRUD 프로그램

---

## 기술 스택

- Java 21
- JDBC (mysql-connector-j-8.4.0)
- MySQL (로컬)
- Java Swing (GUI)

---

## 프로젝트 구조

```
ureka-mini-project/
├── src/cinema/
│   ├── Main.java              # 진입점
│   ├── movie/                 # 영화 관리
│   ├── theater/               # 상영관 관리
│   ├── screening/             # 상영일정 관리
│   ├── customer/              # 고객 관리
│   ├── reservation/           # 예약 관리
│   └── util/                  # DBUtil, ComboItem, sql.properties
├── lib/                       # JDBC 드라이버, Lombok
├── cinema_ddl.sql             # DB 생성 스크립트
└── Makefile                   # 빌드/실행
```

---

## 시작하기 전에 (공통)

### 1. MySQL DB 설정

터미널에서 MySQL 접속 후 DDL 실행:

```bash
mysql -u root -p
```

```sql
source /프로젝트절대경로/cinema_ddl.sql
```

또는 한 줄로:

```bash
mysql -u root -p < cinema_ddl.sql
```

### 2. db.properties 생성

```bash
cp src/cinema/util/db.properties.example src/cinema/util/db.properties
```

`src/cinema/util/db.properties` 열어서 본인 MySQL 정보로 수정:

```properties
url=jdbc:mysql://localhost:3306/cinemasys
user=root
password=여기에_본인_비밀번호
```

> 비밀번호가 없으면 `password=` 비워두면 됩니다.

---

## 실행 방법

### 방법 A — 터미널 (IDE 무관, 가장 간단)

```bash
make run
```

처음 한 번만 실행하면 빌드 + 실행이 동시에 됩니다.

| 명령어 | 설명 |
|--------|------|
| `make run` | 빌드 + 실행 |
| `make build` | 빌드만 |
| `make clean` | bin/ 초기화 |

---

### 방법 B — Eclipse

1. `File > Import > General > Existing Projects into Workspace`
2. `Browse...` → 이 프로젝트 폴더 선택 → `Finish`
3. `src/cinema/Main.java` 우클릭 → `Run As > Java Application`

---

### 방법 C — VS Code

1. [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) 설치
2. 프로젝트 폴더 열기
3. `src/cinema/Main.java` 열기 → 우측 상단 ▶ 클릭

> `.vscode/settings.json`과 `launch.json`이 이미 포함되어 있어 별도 설정 불필요

---

## 주의 사항

- `src/cinema/util/db.properties`는 git에 올라가지 않습니다. **본인이 직접 생성해야 합니다.**
- DB가 실행 중인 상태에서 프로그램을 실행해야 합니다.
- `cinema_ddl.sql`에 샘플 데이터가 포함되어 있습니다.
