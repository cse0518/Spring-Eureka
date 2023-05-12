FROM adoptopenjdk/openjdk8:alpine-slim
COPY build/libs/*SNAPSHOT.jar eureka-client2.jar
ENTRYPOINT ["java", "-jar", "/eureka-client2.jar"]
