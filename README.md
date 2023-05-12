## Index

- [MSA 환경 구현](#msa-환경-구현)
- [Spring Cloud Gateway](#spring-cloud-gateway)
- [Spring Eureka](#spring-eureka)
  - [Eureka Server](#eureka-server)
  - [Eureka Client](#eureka-client)

<br/>

## MSA 환경 구현

![image](https://user-images.githubusercontent.com/60170616/232970280-115d2eb9-b1af-4fea-9c11-95817cca5a6c.png)

Client에서 보낸 요청을 Gateway에서 수신하고, 해당하는 서비스로 라우팅해줍니다.

<br/>

## Spring Cloud Gateway

클라이언트와 백엔드 서비스 사이에서 리버스 프록시 역할을 해줍니다.  
그리고 MSA 전체 서비스에 대한 인증, 모니터링 등 관리 포인트를 추가하여 모든 API의 관문 역할을 해줍니다.

Spring Cloud Gateway는 Spring MVC(Tomcat)가 아닌 Netty를 사용하여 구현했습니다.  
모든 요청이 Gateway를 통과하기 때문에 빠른 처리가 중요합니다.  
Netty는 비동기 WAS이고 1THREAD / MANY REQUESTS 방식이기 때문에 기존 방식보다 더 많은 요청을 처리할 수 있습니다.

<br/>

## Spring Eureka

MSA 기반의 api 서버를 구축하기 위해 spring eureka 기술을 사용했습니다.  
Spring Cloud Gateway - Eureka Server, Client를 구현했습니다.

Eureka는 클라우드 환경에서 다수의 서비스들의 로드 밸런싱 및 장애 조치 목적을 가진 미들웨어 서버입니다.  
MSA 환경에서 수많은 api 서버들로 구성이 될 것이고, 이 서버들을 하나의 게이트웨이를 통해서 관리합니다.

하나의 Eureka Server에서 여러개의 Eureka Client들을 관리합니다.  
Eureka Server는 Eureka Client들을 관리하고,  
Eureka Client들은 다른 Client들의 정보를 확인해 통신할 수 있습니다.

<br/>

### Eureka Server

- discovery server
- eureka client들을 등록
- client들로부터 일정 시간마다 heartbeat를 수신,  
  heartbeat가 오지 않는 경우 client가 죽었다고 판단,  
  일정 시간안에 registry에서 client 정보 삭제
- 유지된 registry 정보를 client들에게 전달

### Eureka Client

- MSA 환경에서 각각의 서비스들은 eureka client가 된다.
- 스스로 자신의 meta 정보를 eureka server에 등록
- 서버로부터 registry 정보를 가져와 캐싱하고 주기적으로 변경사항을 확인한다.  
  이를 통해 다른 client들의 정보를 알 수 있음
