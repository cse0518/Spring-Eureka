#! /bin/sh

echo "# Eureka-Server Gradle Build..."
cd ./eureka-server
./gradlew clean build

echo "# Eureka-Gateway Gradle Build..."
cd ../spring-cloud-gateway
./gradlew clean build

echo "# Eureka-Client1 Gradle Build..."
cd ../eureka-client
./gradlew clean build

echo "# Eureka-Client2 Gradle Build..."
cd ../eureka-client-2nd
./gradlew clean build

echo "# Netty-Server Gradle Build..."
cd ../Netty-Telnet/netty-server
./gradlew clean build

echo "# Netty-Client Gradle Build..."
cd ../netty-client
./gradlew clean build
