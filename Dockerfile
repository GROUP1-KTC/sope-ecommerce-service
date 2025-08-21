# ---------- Stage 1: Build ----------
FROM gradle:8.14-jdk17-alpine AS builder
WORKDIR /app
COPY target/app.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

# Cache dependencies
COPY settings.gradle.kts build.gradle.kts gradle.properties* ./
COPY gradle ./gradle
RUN gradle --no-daemon build -x test || true

# Copy source and build jar
COPY . .
RUN gradle --no-daemon clean bootJar

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
ARG JAR_PATH=/app/build/libs
COPY --from=builder ${JAR_PATH}/*.jar app.jar

EXPOSE 8082

ENV SPRING_PROFILES_ACTIVE=local

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
