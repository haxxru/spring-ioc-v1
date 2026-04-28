# Spring-IoC-v1

간단한 IoC 컨테이너를 직접 구현해 보는 과제입니다.  
`ApplicationContext`가 빈을 생성하고 보관하며, 이름으로 조회할 수 있도록 구성했습니다.

## 🎯 프로젝트 목표

- `ApplicationContext` 객체 생성
- `testPostService` 빈 생성
- `testPostService` 싱글톤 보장
- `testPostRepository` 빈 생성
- `testPostService`에 `testPostRepository` 의존성 주입
- `testFacadePostService`에 `testPostService`, `testPostRepository` 의존성 주입

## 🛠️ 구현 내용

현재 구현은 범용 스프링 컨테이너가 아니라, 테스트 통과를 목표로 한 하드코딩 방식의 간단한 IoC 컨테이너입니다.

`ApplicationContext` 내부에서:

- `TestPostRepository` 객체를 생성
- 생성한 `TestPostRepository`를 주입하여 `TestPostService` 객체를 생성
- 생성한 `TestPostService`, `TestPostRepository`를 주입하여 `TestFacadePostService` 객체를 생성
- 생성된 객체들을 `Map<String, Object>`에 저장
- `genBean(String beanName)` 호출 시 이름에 해당하는 객체를 `Map`에서 꺼내 반환

이 방식으로 같은 이름의 빈을 다시 요청해도 같은 객체를 반환하므로 싱글톤처럼 동작합니다.

## 📦 빈과 싱글톤

### 🌱 빈

빈은 `ApplicationContext`가 생성하고 관리하는 객체입니다.  
이 프로젝트에서는 다음 객체들이 빈입니다.

- `testPostRepository`
- `testPostService`
- `testFacadePostService`

### 🔁 싱글톤

싱글톤은 같은 빈을 여러 번 요청해도 새로운 객체를 만들지 않고, 처음 생성한 객체 하나를 계속 재사용하는 방식입니다.  
이 프로젝트에서는 빈을 `Map`에 저장해두고 다시 조회할 때 같은 객체를 반환하도록 구현했습니다.

## 💡 핵심 코드 설명

`[ApplicationContext.java](src/main/java/com/ll/framework/ioc/ApplicationContext.java)`는 빈을 등록하고 조회하는 역할을 합니다.

```java
private final Map<String, Object> beans = new HashMap<>();
```

빈을 저장하는 공간입니다.  
키는 빈 이름이고 값은 실제 객체입니다.

```java
TestPostRepository testPostRepository = new TestPostRepository();
TestPostService testPostService = new TestPostService(testPostRepository);
TestFacadePostService testFacadePostService = new TestFacadePostService(testPostService, testPostRepository);
```

의존 관계 순서에 맞게 객체를 생성합니다.

```java
beans.put("testPostRepository", testPostRepository);
beans.put("testPostService", testPostService);
beans.put("testFacadePostService", testFacadePostService);
```

생성한 객체를 빈 이름으로 `Map`에 저장합니다.

```java
public <T> T genBean(String beanName) {
    return (T) beans.get(beanName);
}
```

빈 이름을 받아 `Map`에서 해당 객체를 꺼내 반환합니다.

## ✅ 테스트 기준 정리

- `t1`: `ApplicationContext` 객체 생성 확인
- `t2`: `testPostService` 빈 조회 가능
- `t3`: `testPostService`를 여러 번 조회해도 같은 객체 반환
- `t4`: `testPostRepository` 빈 조회 가능
- `t5`: `testPostService` 내부의 `testPostRepository`가 컨테이너의 빈과 동일
- `t6`: `testFacadePostService` 내부의 `testPostService`, `testPostRepository`가 컨테이너의 빈과 동일

## ⚙️ 실행 환경

- Java 25
- Gradle Wrapper
- JUnit 5
- AssertJ

## 🧪 테스트 실행

```bash
bash ./gradlew test
```
