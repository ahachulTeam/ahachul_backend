# Ah Hachul Backend Repo

## 개발 스택

- Spring Boot
- Kotlin
- Mysql, JPA, Data JPA
- Docker, Docker-Compose

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

| 이모티콘 | 문자 | 설명 | 
| :-------: | :---: | :---: |
|:sparkles: | `sparkles` | 기능 개발 및 기능 수정|
|:bug:| `bug` | 버그 해결 |
|:recycle: | `recycle` | 코드 리팩토링
|:memo: | `memo` | 문서 추가 및 수정
|:closed_lock_with_key: | `closed_lock_with_key` | 설정 파일 업데이트
|:adhesive_bandage: | `adhesive_bandage` | 중요하지 않은 이슈 및 오타 수정
|:white_check_mark:| `white_check_mark` | 테스트 코드 추가 및 수정


### 이슈

1. 이슈 생성 후 PR
2. 코드 리뷰를 통한 피드백 후 `approve`
3. `develop` 브랜치 `merge`

## 코딩 컨벤션

- `save action plugin` 를 사용해서 팀 내 코딩 컨벤션 통합

## 배포 파이프라인 구성

- Github Action, AWS, Docker

## ERD & 클래스 다이어그램

- 추후 기획

