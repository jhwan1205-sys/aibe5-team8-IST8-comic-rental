## ⚙️ 개발 환경 및 기술 스택
- **Language**: Java 17
- **Database**: MySQL 8.0.x
- **Build Tool**: Gradle
- **VCS**: Git & GitHub

---
## 🚀 구현한 기능

### 1. 아키텍처 및 시스템 설계
* **MVC 패턴 적용:** `Controller`(입출력), `Service`(비즈니스 로직), `Repository`(DB 통신)로 계층을 완벽히 분리하였습니다.
* **의존성 주입 (DI):** `App.java`에서 각 계층의 구현체를 생성하여 주입함으로써 객체 간의 결합도를 낮췄습니다.
* **글로벌 예외 처리:** 하위 계층에서 발생한 예외(DB 오류, 검증 실패 등)를 최상단 로직으로 던져(Throw) 프로그램의 강제 종료를 막고 일관된 에러 메시지를 출력합니다.

### 2. 도메인별 핵심 기능
* **👤 회원 관리 (Member)**
  * 회원 등록 (이름, 전화번호) 및 전체 목록 조회
  * **데이터 검증:** 전화번호 형식(정규식) 예외 처리 완벽 적용 (`010-XXXX-XXXX` 또는 `02-XXX-XXXX` 등)
* **📖 만화책 관리 (Comic)**
  * 만화책 등록, 전체 조회, 상세 조회, 수정, 삭제 기능 구현
* **🤝 대여 및 반납 (Rental)**
  * 특정 회원에게 만화책 대여 및 반납 처리 로직 구현
  * **상태 동기화 로직:** 대여/반납 발생 시 `Comic` 테이블의 `isRented` 상태값을 DB에서 실시간으로 변경하고 검증합니다.
  * 이미 대여 중인 도서, 존재하지 않는 회원/도서, 이미 반납된 내역에 대한 방어 로직이 적용되어 있습니다.

---

## ⌨️ 사용 가능한 명령어

| 카테고리 | 명령어 | 설명 | 사용 예시 |
|---|---|---|---|
| **만화책** | `comic-add` | 신규 만화책 등록 | `comic-add` |
| | `comic-list` | 전체 만화책 목록 조회 | `comic-list` |
| | `comic-detail [번호]` | 특정 만화책 상세 조회 | `comic-detail 1` |
| | `comic-update [번호]` | 특정 만화책 정보 수정 | `comic-update 1` |
| | `comic-delete [번호]` | 특정 만화책 삭제 | `comic-delete 1` |
| **회원** | `member-add` | 신규 회원 등록 | `member-add` |
| | `member-list` | 전체 회원 목록 조회 | `member-list` |
| **대여/반납** | `rent [회원번호] [만화번호]`| 특정 회원에게 만화책 대여 | `rent 1 2` |
| | `return [대여번호]` | 만화책 반납 처리 | `return 1` |
| | `rental-list` | 전체 대여 및 반납 내역 조회 | `rental-list` |
| **시스템** | `exit` | 프로그램 종료 | `exit` |

---

## 💻 실행 예시

```text
===만화 대여 프로그램 시작===
명령어 : member-add
이름: 에밀리
전화번호: 010-1111-2222
=> 회원이 등록되었습니다. (id=1)

명령어 : comic-add
제목: 슬램덩크
권수: 1
작가: 이노우에 다케히코
=> 만화책이 등록되었습니다. (id=1)

명령어 : comic-list
번호 | 제목       | 권수 | 작가               | 상태 | 등록일
-------------------------------------------------------------
1    | 슬램덩크   | 1    | 이노우에 다케히코   | 대여가능 | 2026-03-03

명령어 : rent 1 1
=> 대여 완료: [대여id=1] 만화(1) → 회원(1)

명령어 : rental-list
대여id | 만화id | 회원id | 대여일     | 반납일
-----------------------------------------------
1     | 1      | 1      | 2026-03-03 | -

명령어 : return 1
=> 반납 완료: 대여id=1

명령어 : exit
프로그램을 종료합니다.
```

## 🗄️ 데이터베이스 세팅 (Getting Started)

### 1. DB 접속 정보 설정
`db.properties` 파일은 `.gitignore`에 등록되어 있어 GitHub에 올라가지 않으므로 각자 로컬에서 만들어야 합니다.

1. `src/main/resources/` 경로에 있는 `db.properties.sample` 파일의 이름을 `db.properties`로 변경합니다.
2. `db.properties` 파일을 열고 아래와 같이 본인의 MySQL 비밀번호를 입력합니다.

```properties
db.url=jdbc:mysql://localhost:3306/comic_rental_db
db.user=root
db.password=본인의_MySQL_비밀번호_입력
```

### 2. DB 및 테이블 생성 (DDL)
MySQL 클라이언트(Workbench, 터미널 등)에서 아래의 SQL 스크립트를 실행하여 스키마를 구성합니다.

```sql
-- 데이터베이스 생성 및 사용
CREATE DATABASE comic_rental_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE comic_rental_db;

-- 만화책(Comic) 테이블
CREATE TABLE comic(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	title VARCHAR(200) NOT NULL,
	volume INT NOT NULL,
	author VARCHAR(100),
	isrented BOOLEAN DEFAULT FALSE,
	regdate DATE DEFAULT NOW()
);

-- 회원(Member) 테이블
CREATE TABLE member(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	phone VARCHAR(20),
	regdate DATE DEFAULT NOW()
);

-- 대여(Rental) 테이블
CREATE TABLE rental(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	comicId BIGINT NOT NULL,
	memberId BIGINT NOT NULL,
	rentalDate DATE DEFAULT NOW(),
	returnDate DATE NULL,
	FOREIGN KEY (comicId) REFERENCES comic (id),
	FOREIGN KEY (memberId) REFERENCES member (id)
);
```
---




## 📂 프로젝트 파일 구조 (Directory Structure)

본 프로젝트는 객체지향의 역할 분담과 향후 프레임워크 도입을 고려하여 도메인 및 MVC(Controller-Service-Repository) 패턴 기반으로 디렉토리를 분리하여 작업합니다.

```text
comic-rental/
├── .idea/                      # IntelliJ 프로젝트 설정 폴더 (Git에 안 올라감)
├── gradle/wrapper/             # Gradle 빌드 도구 관련 폴더
│
├── src/main/
│   ├── java/org/example/      
│   │   ├── Main.java           # 프로그램의 진짜 시작점 (App 객체 생성 및 실행)
│   │   ├── App.java            # 명령어 무한 루프(while) 및 분기 처리, 입출력 담당
│   │   ├── Rq.java             # 사용자 입력 명령어(Request) 분석 유틸
│   │   │
│   │   ├── db/                 # 공통 DB 인프라
│   │   │   └── DBUtil.java     # DB 연결 및 종료 유틸리티
│   │   │
│   │   ├── comic/              # 📚 만화책 도메인
│   │   │   ├── Comic.java              # 엔티티(DTO): 만화책 데이터 껍데기
│   │   │   ├── ComicService.java       # 핵심 비즈니스 로직 및 기능 수행
│   │   │   └── ComicRepository.java    # DB에 INSERT/SELECT 쿼리 날리기
│   │   │
│   │   ├── member/             # 👤 회원 도메인
│   │   │   └── (Service, Repository, DTO 등 생성 예정)
│   │   │
│   │   └── rental/             # 🔄 대여/반납 도메인
│   │       └── (Service, Repository, DTO 등 생성 예정)
│   │
│   └── resources/              # ⚙️ 설정 파일들이 들어가는 곳
│       ├── db.properties         # (Git 제외) 진짜 내 DB 비밀번호
│       └── db.properties.sample  # (Git 포함) 팀원 공유용 샘플 파일
│
├── .gitignore                  # Git에 올리지 않을 파일 목록
├── build.gradle                # 의존성, 인코딩 옵션 등을 적어둔 빌드 설정 파일
├── gradlew, gradlew.bat        # 터미널용 빌드 스크립트
└── README.md                   # 📄 프로젝트 설명서
```

---

## ERD
<img width="660" height="489" alt="image" src="https://github.com/user-attachments/assets/184e887f-82d2-4faf-ac65-24ce5c5fd19f" />

---

## 🤝 팀 협업 컨벤션 (Team Collaboration Convention)

우리 팀의 안전하고 효율적인 프로젝트 진행을 위한 협업 가이드입니다.
모든 작업은 **Issue 생성 ➔ Branch 생성 ➔ 작업 및 Commit ➔ PR 및 Merge** 순서로 진행됩니다. `main` 브랜치에 직접 작업하는 것은 절대 금지입니다!


### 1. 🎫 이슈(Issue) 및 브랜치(Branch) 규칙
작업을 시작하기 전, 반드시 **이슈를 먼저 생성**하고 해당 이슈와 연결된 브랜치에서 작업합니다.

1. **이슈 생성:** GitHub `Issues` 탭에서 작업할 내용의 이슈를 생성합니다.
2. **브랜치 생성:** 이슈 화면 우측의 `Create a branch`를 클릭하여 브랜치를 생성하거나, 로컬에서 직접 생성합니다.
3. **브랜치 명명 규칙:** `작업타입/이슈번호-기능요약` 
    * `feat/` : 새로운 기능 추가 (예: `feat/1-comic-add`)
    * `fix/` : 버그 수정 (예: `fix/3-rental-error`)
    * `docs/` : 문서 수정 (예: `docs/5-readme-update`)
    * `refactor/` : 코드 구조 개선 (기능 변경 없음)
    * `chore/` : 설정, 빌드 등 기타 작업

### 2. 📝 커밋 메시지 (Commit Message) 규칙
커밋 메시지는 "무엇을" 했는지 명확하게 작성하며, 작업 추적을 위해 **관련 이슈 번호를 태그**합니다.

* **형식:** `타입: 작업 내용 요약 (#이슈번호)`
* **작업 타입 가이드:** `feat`, `fix`, `chore`, `docs`, `style`
* **예시:**
    * `feat: 만화책 등록(comic-add) 로직 구현 (#1)`
    * `fix: 대여 시 발생하는 NullPointerException 오류 해결 (#3)`
    * `chore: DB 연결을 위한 db.properties 세팅 (#2)`

### 3. 🚀 Pull Request (PR) 및 머지(Merge) 규칙
기능 개발이 완료되면 PR을 통해 팀원들에게 코드를 공유하고 리뷰를 받습니다.

1. **PR 생성:** 작업 브랜치에서 `main` 브랜치를 향해 PR을 생성합니다.
2. **이슈 자동 종료:** PR 내용 본문에 `Resolves #이슈번호` (예: `Resolves #1`)를 적어두면, 머지될 때 이슈가 자동으로 닫힙니다.
3. **코드 리뷰:** 최소 **1명 이상**의 팀원이 코드를 읽고 **Approve(승인)**를 해야만 머지할 수 있습니다.
4. **로컬 최신화:** 누군가의 PR이 `main`에 머지되어 단톡방에 공유되면, 나머지 팀원들은 즉시 터미널에서 `git pull origin main`을 실행하여 로컬 환경을 최신 상태로 유지합니다.

### 4. 💻 코딩 스타일 및 네이밍 규칙
서로의 코드를 쉽게 읽기 위한 자바/DB 기본 네이밍 약속입니다.

* **클래스명:** 대문자로 시작하는 `PascalCase` (예: `ComicController`, `Member`)
* **변수명/메서드명:** 소문자로 시작하는 `camelCase` (예: `isRented`, `getComicList()`)
* **상수명:** 대문자와 언더스코어 `UPPER_SNAKE_CASE` (예: `MAX_RENTAL_COUNT`)
