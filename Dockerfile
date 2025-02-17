FROM openjdk:21-jdk-slim AS builder
WORKDIR /app

COPY . .

RUN chmod +x gradlew && ./gradlew build -x test

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
