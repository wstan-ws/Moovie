FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /src

COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

RUN mvn package -Dmaven.test.skip=true

FROM openjdk:21-bookworm

WORKDIR /app

COPY --from=builder /src/target/privatemoviebooking-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080
ENV SPRING_REDIS_HOST=localhost
ENV SPRING_REDIS_PORT=6379
ENV SPRING_REDIS_DATABASE=0
ENV SPRING_REDIS_USERNAME=default
ENV SPRING_REDIS_PASSWORD=abc123
ENV MOVIEAPI_KEY=abc123

ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar