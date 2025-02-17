FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY . .

RUN chmod +x gradlew && ./gradlew build -x test

RUN mkdir -p /app/build_output && cp build/libs/*.jar /app/build_output/app.jar

FROM eclipse-temurin:21-jre-slim
WORKDIR /app

COPY --from=builder /app/build_output/app.jar app.jar

CMD ["java", "-jar", "app.jar"]
