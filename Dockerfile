# ---------- Stage 1: Build ----------
FROM gradle:8.14-jdk17-alpine AS builder
WORKDIR /app

# Copy file build.gradle / settings.gradle / gradle wrapper
COPY settings.gradle.kts build.gradle.kts gradle.properties* ./
COPY gradle ./gradle
RUN gradle --no-daemon build -x test || true
# Copy source code
COPY . .
RUN gradle --no-daemon clean bootJar

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy jar từ stage builder
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
