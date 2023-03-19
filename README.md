# Ah Hachul Backend Repo

## 개발 스택

- Spring Boot
- Kotlin
- Mysql, JPA
- Docker

## 패키지 구조

```javascript
|-- ahachul_backend
    |-- <도메인>
        |-- adapter
            |-- in
            |-- out
        |-- application
            |-- port
                |-- in
                |-- out
            |-- service
        |-- domain
    |-- common
        |-- config
        |-- ...
```

## 브랜치 전략

```javascript
|-- main
    |-- develop
        |-- feature/<#issue number>
    |-- hotfix
```

## 협업 규칙

### 커밋 메시지

- [gitmoji 공식문서](https://gitmoji.dev/)
- **`gitmoji <commit message> (#issue number)`**

### 이슈

1. 이슈 생성 후 PR
1. 피드백 후 approve
1. merge

## 코딩 컨벤션

- save action plugin

## 배포 파이프라인 구성

- Git Action, AWS, Docker

## ERD & 클래스 다이어그램

- 추후 기획

