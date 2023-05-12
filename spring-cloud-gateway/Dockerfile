FROM adoptopenjdk/openjdk8:alpine
COPY build/libs/*SNAPSHOT.jar eureka-gateway.jar
ENTRYPOINT ["java", "-jar", "/eureka-gateway.jar"]
