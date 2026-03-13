## ⚙️ 개발 환경 및 기술 스택
- **Language**: Java 17
- **Database**: MySQL 8.0.x
- **Build Tool**: Gradle
- **VCS**: Git & GitHub

---

## 🗄️ 데이터베이스 세팅 (Getting Started)
프로젝트를 로컬 환경에서 실행하기 위해 아래의 SQL 스크립트를 MySQL에서 먼저 실행해 주세요.
```sql
CREATE DATABASE comic_rental_db;
```

###  DB 접속 정보 설정
`db.properties` 파일은 `.gitignore`에 등록되어 있어 GitHub에 올라가지 않으므로 각자 로컬에서 만들어야 합니다.

1. `src/main/resources/` 경로에 있는 `db.properties.sample` 파일의 이름을 `db.properties`로 변경합니다.
2. `db.properties` 파일을 열고 아래와 같이 본인의 MySQL 비밀번호를 입력합니다.

```properties
db.url=jdbc:mysql://localhost:3306/comic_rental_db
db.user=root
db.password=본인의_MySQL_비밀번호_입력
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
