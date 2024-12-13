FROM maven:3.9.4-eclipse-temurin-17 AS build
FROM eclipse-temurin:17-jdk-alpine

COPY src ./app/src
COPY pom.xml ./app

WORKDIR /app
RUN mvn clean package
WORKDIR /app

COPY --from=build /app/target/api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
