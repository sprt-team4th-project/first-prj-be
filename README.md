# 🛒 고객 및 상품 관리 시스템 (E-Commerce Back-office)

> **팀원들의 원활한 협업과 효율적인 관리를 위한 이커머스 백오피스 프로젝트입니다.**

---

## 🏗 기술 스택 및 구조 (Tech Stack)

![Java](https://img.shields.io/badge/Java%2017-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-%234479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

| 기술 스택 | 버전 |
| :--- | :---: |
| Java | 17 |
| Spring Boot | 4.0.2 |
| Spring Data JPA | Spring Boot 관리 |
| MySQL | 8.x |
| io.spring.dependency-management | 1.1.7 |

- **Architecture**: Domain-Driven Design (Partial), Layered Architecture (Query/Command Service)

---

## 👨‍💻 팀원 소개 및 역할 분담

### **R&R (Role & Responsibilities)**

| 이름 | 역할 | 담당 과업 (Task) |
| :---: | :---: | :--- |
| **정채림** | **Leader** | 프로젝트 총괄, 일정 관리, 초기 환경 세팅(Git/S.A), 공통 Response/Exception 핸들러, 코드 병합 및 최종 QA |
| **정민교** | **Presenter** | 발표 자료 제작 및 리허설 주도, 주문 조회(검색/정렬/페이징), 회의록 및 README 관리 |
| **이지민** | **Sub Leader** | 관리자 인증(회원가입/승인/세션 로그인), 도메인 로직 설계 및 코드 리뷰 주도 |
| **신현민** | **Sub Leader** | 상품 관리 총괄(등록/조회/수정/삭제), 재고 기반 자동 상태 변경 로직 구현 |
| **이재민** | **Sub Leader** | 주문 CUD(생성/취소), 트랜잭션 관리 및 재고 동시성 제어 핵심 담당 |
| **이지혜** | **Recorder** | 고객 관리(조회/수정/삭제/상태 변경), 회의록 정리 및 문서화 보조 |

---

## 📋 프로젝트 개요

본 프로젝트는 이커머스 운영에 필수적인 **관리자 전역 기능, 고객 응대, 상품 및 재고 관리, 그리고 주문 프로세스**를 효율적으로 처리하기 위한 백오피스 시스템입니다.

### **주요 특징**
- **계층별 책임 분리**: Query/Command Service 분리를 통한 가독성 및 유지보수성 향상
- **데이터 정합성**: 주문 시 재고 차감 및 취소 시 복구 로직의 트랜잭션 보장
- **확장성 있는 설계**: 공통 DTO와 페이징 처리를 통한 일관된 API 구조

---

## 🗂 ERD (Entity Relationship Diagram)

<img width="1280" height="842" alt="Image" src="https://github.com/user-attachments/assets/fb60e0e9-4fcb-48e9-a3d2-b563e266be9d" />

### 데이터베이스 구조

본 프로젝트는 관리자 기반의 커머스 백오피스 시스템을 가정하여 설계되었습니다.
관리자는 상품과 주문을 관리하고, 고객은 상품을 주문하는 구조를 가집니다.

#### 주요 엔티티 관계

| 관계 | 설명 |
| :--- | :--- |
| **Customer - Order (1:N)** | 한 명의 고객은 여러 개의 주문을 생성할 수 있습니다. |
| **Admin - Product (1:N)** | 관리자는 여러 상품을 등록 및 수정하며 상품 관리의 책임을 가집니다. |
| **Admin - Order (1:N)** | 주문은 관리자에 의해 처리(승인/취소/관리)됩니다. |
| **Product - Order (1:N)** | 하나의 상품은 여러 주문에 포함될 수 있습니다. |
| **Category - Product (1:N)** | 하나의 카테고리는 여러 상품을 포함합니다. |
| **Category - Category (Self-Reference, 1:N)** | 카테고리는 부모 카테고리를 가질 수 있어 계층형(대분류/중분류/소분류) 구조를 지원합니다. |
| **SuperAdmin - Admin (1:N)** | 상위 관리자는 여러 관리자를 생성 및 권한 관리할 수 있습니다. |

---

## 📡 API 엔드포인트

### 1. 슈퍼관리자 (SuperAdmin)

| Method | Endpoint | 설명 | 권한 |
| :---: | :--- | :--- | :---: |
| `GET` | `/api/admins` | 관리자 리스트 조회 | 슈퍼관리자 |
| `GET` | `/api/admins/{adminId}` | 관리자 상세 조회 | 슈퍼관리자 |
| `PATCH` | `/api/admins/{adminId}` | 관리자 정보 수정 | 슈퍼관리자 |
| `PATCH` | `/api/admins/{adminId}/role` | 관리자 역할 변경 | 슈퍼관리자 |
| `PATCH` | `/api/admins/{adminId}/status` | 관리자 상태 변경 | 슈퍼관리자 |
| `DELETE` | `/api/admins/{adminId}` | 관리자 삭제 | 슈퍼관리자 |

### 2. 관리자 (Admin)

| Method | Endpoint | 설명 | 권한 |
| :---: | :--- | :--- | :---: |
| `POST` | `/api/admins/signup` | 관리자 회원가입 (이메일, 비밀번호 암호화 저장) | 누구나 |
| `POST` | `/api/admins/login` | 관리자 로그인 (세션 ID 발급) | 누구나 |
| `POST` | `/api/admins/logout` | 관리자 로그아웃 | 로그인 |
| `PATCH` | `/api/admins/me/password` | 비밀번호 변경 | 본인 |

### 3. 주문 (Order)

| Method | Endpoint | 설명 | 권한 |
| :---: | :--- | :--- | :---: |
| `GET` | `/api/orders` | 주문 전체 조회 | 로그인 필수 |
| `GET` | `/api/orders/{orderId}` | 주문 상세 조회 | 로그인 필수 |
| `PUT` | `/api/orders/{orderId}` | 주문 상세 수정 | 로그인 필수 |
| `DELETE` | `/api/orders/{orderId}` | 주문 단건 삭제 | 로그인 필수 |
| `POST` | `/api/orders` | CS 주문 생성 | CS 관리자 |

### 4. 카테고리 (Category)

| Method | Endpoint | 설명 | 권한 |
| :---: | :--- | :--- | :---: |
| `POST` | `/api/categories` | 카테고리 작성 | 관리자 |
| `GET` | `/api/categories` | 카테고리 전체 조회 | 누구나 |
| `GET` | `/api/categories/{categoryId}` | 단일 카테고리 조회 | 누구나 |
| `PATCH` | `/api/categories/deleted` | 삭제된 카테고리 조회 | 관리자 |
| `PUT` | `/api/categories/{categoriesId}` | 카테고리 수정 | 관리자 |
| `PUT` | `/api/categories/{categoriesId}/restore` | 카테고리 복원 | 관리자 |
| `DELETE` | `/api/categories/{categoriesId}` | 카테고리 삭제 (Soft Delete) | 관리자 |

### 5. 상품 (Product)

| Method | Endpoint | 설명 | 권한 |
| :---: | :--- | :--- | :---: |
| `POST` | `/api/products` | 상품 작성 | 관리자 |
| `GET` | `/api/products` | 상품 전체 조회 | 누구나 |
| `GET` | `/api/products/{productId}` | 단일 상품 조회 | 누구나 |
| `PATCH` | `/api/products/{productId}` | 상품 정보 수정 | 관리자 |
| `PATCH` | `/api/products/{productId}/stock` | 상품 재고 변경 | 관리자 |
| `PATCH` | `/api/products/{productId}/stock/increase` | 상품 재고 추가 | 관리자 |
| `PATCH` | `/api/products/{productId}/stock/decrease` | 상품 재고 감소 | 관리자 |
| `PATCH` | `/api/products/{productId}/stock/discontinue` | 상품 단종 | 관리자 |
| `DELETE` | `/api/products/{productId}` | 상품 삭제 (Soft Delete) | 관리자 |

### 6. 고객 (Customer)

| Method | Endpoint | 설명 | 권한 |
| :---: | :--- | :--- | :---: |
| `GET` | `/api/customers?keyword=customer&status=DELETED` | 고객 목록 조회 | 관리자 |
| `GET` | `/api/customers/{customerId}` | 단일 고객 조회 | 관리자 |
| `PATCH` | `/api/customers/{customerId}` | 고객 정보 수정 | 관리자 |
| `PATCH` | `/api/customers/{customerId}/status` | 고객 상태 수정 | 관리자 |
| `DELETE` | `/api/customers/{customerId}` | 고객 삭제 (Soft Delete) | 관리자 |

---

## 🛠 주요 구현 기능

### 1. Auth & Admin (관리자)
- **인증**: 관리자 회원가입, 세션 기반 로그인/로그아웃 구현
- **계정 관리**: 관리자 목록 조회, 승인/거부 프로세스, 권한 및 상태 변경
- **마이페이지**: 프로필 조회/수정 및 비밀번호 변경 로직(도메인 책임 분리)

### 2. Customer (고객 관리)
- 고객 정보 목록 및 상세 조회
- 고객 정보 수정 및 삭제(Soft Delete 적용)
- 고객 활동 상태 관리

### 3. Product & Category (상품 및 카테고리)
- **상품 관리**: 등록/수정/삭제 및 재고 변경 시 수량에 따른 자동 상태 전환(품절 등)
- **카테고리**: 카테고리 CRUD를 통한 상품 분류 체계 구축
- **조회**: 필터링, 검색, 페이징이 적용된 상품 목록 조회

### 4. Order (주문 관리)
- **주문 생성**: 주문 시 재고 검증 및 실시간 재고 차감
- **주문 취소**: 사유 입력 필수, 취소 시 재고 자동 복구 트랜잭션 처리
- **운영**: 주문 상태 변경 및 상세 내역 추적

### 5. Common (공통 기능)
- 전역 예외 처리(Global Exception Handler) 및 공통 응답 규격 적용
- Query Parameter 기반의 동적 검색 및 정렬, 페이징 처리

---

## 📁 프로젝트 구조
```
CommercePilot/
├── src/main/java/com/example/commercepilot/
│   ├── admin/                      # 관리자 도메인
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   ├── response/
│   │   │   └── session/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── service/
│   ├── category/                   # 카테고리 도메인
│   ├── customer/                   # 고객 도메인
│   ├── orders/                     # 주문 도메인
│   ├── product/                    # 상품 도메인
│   ├── config/                     # 전역 설정
│   │   ├── BaseEntity
│   │   └── PasswordEncoder
│   ├── exception/                  # 예외/공통 응답 처리
│   │   ├── ApiResponse
│   │   ├── CustomException
│   │   ├── ErrorCode
│   │   ├── ErrorResponse
│   │   ├── FieldError
│   │   └── GlobalExceptionHandler
│   └── CommercePilotApplication.java
├── src/main/resources/
├── src/test/
└── build.gradle
```

---

## ⚙️ 설치 및 실행

### 1. 필수 요구사항
- Java 17 이상
- MySQL 8.x 이상
- Gradle (프로젝트에 포함된 Wrapper 사용 권장)

### 2. 프로젝트 클론
```bash
git clone https://github.com/sprt-team4th-project/first-prj-be.git
cd first-prj-be
```

### 3. 데이터베이스 설정
```sql
-- MySQL에 데이터베이스 생성
CREATE DATABASE CommercePilot DEFAULT;
```

> `ddl-auto=update` 설정으로 애플리케이션 실행 시 엔티티 기준으로 테이블이 자동 생성/갱신됩니다.

### 4. 애플리케이션 설정

`src/main/resources/application.properties`
```properties
spring.application.name=CommercePilot

spring.datasource.url=jdbc:mysql://localhost:3306/CommercePilot
spring.datasource.username=user-name
spring.datasource.password=user-password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs

server.servlet.session.timeout=24h
server.servlet.session.cookie.http-only=true
```

> 사용자 환경에 맞게 `username`, `password`를 수정해주세요.

### 5. 애플리케이션 실행

Gradle로 실행:
```bash
./gradlew bootRun
```

또는 빌드 후 JAR 실행:
```bash
./gradlew build
java -jar build/libs/CommercePilot-0.0.1-SNAPSHOT.jar
```

> JAR 파일명은 프로젝트 버전에 따라 달라질 수 있습니다. (`build/libs` 폴더 확인)

### 6. 접속 확인

| 항목 | URL |
| :--- | :--- |
| API 기본 주소 | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI Docs (JSON) | http://localhost:8080/v3/api-docs |

---

## 🚨 트러블 슈팅 (Troubleshooting)

### 1. 🛡️ 엄격한 협업 룰과 Git 브랜치 전략
- **Issue & Background**: 다수의 팀원이 동시에 코드를 병합하는 과정에서, 컴파일 에러가 포함된 불완전한 코드가 공용 브랜치(`develop`)에 반영될 수 있는 위험성을 인지함.
- **Solution**:
  - `develop` 브랜치 병합 전, 로컬 환경에서 테스트를 완료하여 컴파일 에러가 없는지 확인하는 규칙 수립.
  - 에러 발생 시 강제로 머지하지 않고 별도 브랜치에서 수정 후 다시 PR을 올림.
  - 병합 후 긴급 에러는 `fix` 브랜치(Hotfix)를 통해 빠르게 대응.
- **Insight**: 깨진 코드가 공용 브랜치에 올라가면 팀 전체의 개발 진행이 멈추는 병목 현상이 발생함. PR 전 꼼꼼한 사전 테스트가 팀 전체의 개발 속도와 안정성을 높이는 가장 확실한 방법임을 깨달음.

---

## 🔄 프로젝트 회고 (Retrospective)

### 🗣️ 팀원별 KPT 및 회고
- **정채림 (Leader)**: 팀원 간 소통과 배려에 만족하며, 앞으로도 정해진 컨벤션을 철저히 지켜야겠다고 다짐함.
- **이재민 (Sub Leader)**: 잦은 소통과 실수 없는 Git 활용에 만족함. 팀 협업의 안정성을 유지하는 데 기여하여 뜻깊음.
- **이지민 (Sub Leader)**: 레이어 책임 분리와 코드 개선 경험에 만족함. 설계 단계에서 도메인 책임과 세션 정의를 더 깊이 고민해 볼 계획.
- **정민교 (Presenter)**: 레이어 책임 분리와 코드 개선에 전반적으로 만족함. 향후 프로젝트 초기 설계 단계에서 도메인 책임 할당과 세션 정의에 대해 더 깊은 고민이 필요함을 체감함.
- **신현민 (Sub Leader)**: 공통 DTO를 활용한 코드 가독성 향상을 체감함. 앞으로도 원활한 소통을 유지하고 기술적 기본 구성을 탄탄히 하는 데 주력할 예정.
- **이지혜 (Recorder)**: 패키지 구성 및 기능 구현 경험에 만족함. 앞으로 Git 조작에 더욱 주의를 기울이겠다고 다짐함.
