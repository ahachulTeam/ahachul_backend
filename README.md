<div align="center">

# 🚀 AhHachul Backend

**🧜‍♂️ 더욱 쾌적한 지하철을 위한 유저 기반 커뮤니티 플랫폼, ..아... 하철이형! 🧜**

<br />

<img width="1424" alt="image" src="https://github.com/ahachulTeam/ahachul_web/assets/80245801/d29c819c-6b95-4a77-838a-3bdc92a297fb">
</div>

<br />
</div>

## 개발 스택

- Spring Boot
- Kotlin
- Mysql, JPA, Data JPA, QueryDSL
- Spring REST Docs, Junit5, Mockito


## ERD 
[ERDCloud 링크](https://www.erdcloud.com/d/6dKc9AeJrWc2ZQRNv)

## 배포 파이프라인 구성
- Github Action, AWS, Docker
<img width="777" alt="스크린샷 2024-01-19 오후 2 21 34" src="https://github.com/ahachulTeam/ahachul_backend/assets/71436576/b5337394-21b6-4d01-a89b-3ececbcd6eae">


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

