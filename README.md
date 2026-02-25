# 🛒 고객 및 상품 관리 시스템 (E-Commerce Back-office)

> **팀원들의 원활한 협업과 효율적인 관리를 위한 이커머스 백오피스 프로젝트입니다.**

---

## 🏗 기술 스택 및 구조 (Tech Stack)

![Java](https://img.shields.io/badge/Java%2017-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-%234479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

- **Language/Framework**: Java 17, Spring Boot
- **Database**: Spring Data JPA, MySQL
- **Architecture**: Domain-Driven Design (Partial), Layered Architecture (Query/Command Service)

---

## 👨‍💻 팀원 소개 및 역할 분담

### **R&R (Role & Responsibilities)**

| 이름 | 역할 | 담당 과업 (Task) |
| :---: | :---: | :--- |
| <nobr>**정채림**</nobr> | <nobr>**Leader**</nobr> | 프로젝트 총괄, 일정 관리, 초기 환경 세팅(Git/S.A), 공통 Response/Exception 핸들러, 코드 병합 및 최종 QA |
| <nobr>**정민교**</nobr> | <nobr>**Presenter**</nobr> | 발표 자료 제작 및 리허설 주도, 주문 조회(검색/정렬/페이징), 회의록 및 README 관리 |
| <nobr>**이지민**</nobr> | <nobr>**Sub Leader**</nobr> | 관리자 인증(회원가입/승인/세션 로그인), 도메인 로직 설계 및 코드 리뷰 주도 |
| <nobr>**신현민**</nobr> | <nobr>**Sub Leader**</nobr> | 상품 관리 총괄(등록/조회/수정/삭제), 재고 기반 자동 상태 변경 로직 구현 |
| <nobr>**이재민**</nobr> | <nobr>**Sub Leader**</nobr> | 주문 CUD(생성/취소), 트랜잭션 관리 및 재고 동시성 제어 핵심 담당 |
| <nobr>**이지혜**</nobr> | <nobr>**Recorder**</nobr> | 고객 관리(조회/수정/삭제/상태 변경), 회의록 정리 및 문서화 보조 |

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

## 🚨 트러블 슈팅 (Troubleshooting)

### 1. 🛡️ 엄격한 협업 룰과 Git 브랜치 전략
- **Issue & Background**: 다수의 팀원이 동시에 코드를 병합하는 과정에서, 컴파일 에러가 포함된 불완전한 코드가 공용 브랜치(`develop`)에 반영될 수 있는 위험성을 인지함.
- **Solution (작업 내용 및 합의)**: 
  - `develop` 브랜치 병합 전, 무조건 본인의 로컬 환경(IntelliJ)에서 테스트를 완료하여 컴파일 에러가 없는지 확인하는 규칙 수립.
  - 에러 발생 시 강제로 머지하지 않고 별도 브랜치에서 수정 후 다시 PR(Pull Request)을 올림.
  - 병합 후 예기치 않게 발생한 긴급 에러는 `fix` 브랜치(Hotfix)를 통해 빠르게 대응.
- **Insight (배운 점)**: 깨진 코드가 공용 브랜치에 올라가면 팀 전체의 개발 진행이 멈추는 심각한 병목 현상(Bottleneck)이 발생함. 조금 번거롭더라도 PR 전 꼼꼼한 사전 테스트가 결국 팀 전체의 개발 속도와 프로젝트 안정성을 높이는 가장 확실한 길임을 깨달음.

---

## 🔄 프로젝트 회고 (Retrospective)

### 🗣️ 팀원별 KPT 및 회고
- **정채림 (Leader)**: 팀원 간 소통과 배려에 만족하며, 앞으로도 정해진 컨벤션을 철저히 지켜야겠다고 다짐함.
- **이재민 (Sub Leader)**: 잦은 소통과 실수 없는 Git(GitHub) 활용에 만족함. 팀 협업의 안정성을 유지하는 데 기여하여 뜻깊음.
- **이지민 (Sub Leader)**: 레이어 책임 분리와 코드 개선 경험에 만족함. 설계 단계에서 도메인 책임과 세션 정의를 더 깊이 고민해 볼 계획.
- **정민교 (Presenter)**: 레이어 책임 분리와 코드 개선에 전반적으로 만족함. 향후 프로젝트 초기 설계 단계에서 도메인의 책임 할당과 세션 정의에 대해 더 깊은 고민이 필요함을 체감함.
- **신현민 (Sub Leader)**: 공통 DTO를 활용한 코드 가독성 향상을 체감함. 앞으로도 속도에 맞춘 원활한 소통을 유지하고 기술적 기본 구성을 탄탄히 하는 데 주력할 예정.
