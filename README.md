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

###  DB 접속 정보 설정
`db.properties` 파일은 `.gitignore`에 등록되어 있어 GitHub에 올라가지 않으므로 각자 로컬에서 만들어야 합니다.

1. `src/main/resources/` 경로에 있는 `db.properties.sample` 파일의 이름을 `db.properties`로 변경합니다.
2. `db.properties` 파일을 열고 아래와 같이 본인의 MySQL 비밀번호를 입력합니다.

```properties
db.url=jdbc:mysql://localhost:3306/comic_rental_db
db.user=root
db.password=본인의_MySQL_비밀번호_입력
