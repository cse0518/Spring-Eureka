#! /bin/sh

username="cse0518"
version="ver_1"

echo "# Eureka-Server Docker Image Build..."
cd ./eureka-server
docker build --no-cache -t ${username}/eureka-server:${version} .
echo "# Docker Build Success ------------------------------------------"

echo "# Eureka-Gateway Docker Image Build..."
cd ../spring-cloud-gateway
docker build --no-cache -t ${username}/eureka-gateway:${version} .
echo "# Docker Build Success ------------------------------------------"

echo "# Eureka-Client1 Docker Image Build..."
cd ../eureka-client
docker build --no-cache -t ${username}/eureka-client1:${version} .
echo "# Docker Build Success ------------------------------------------"

echo "# Eureka-Client2 Docker Image Build..."
cd ../eureka-client-2nd
docker build --no-cache -t ${username}/eureka-client2:${version} .
echo "# Docker Build Success ------------------------------------------"

echo "# Netty-Server Docker Image Build..."
cd ../Netty-Telnet/netty-server
docker build --no-cache -t ${username}/netty-server:${version} .
echo "# Docker Build Success ------------------------------------------"

echo "# Netty-Client Docker Image Build..."
cd ../netty-client
docker build --no-cache -t ${username}/netty-client:${version} .
echo "# Docker Build Success ------------------------------------------"
