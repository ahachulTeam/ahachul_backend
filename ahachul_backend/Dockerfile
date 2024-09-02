FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN SPRING_PROFILES_ACTIVE=test ./gradlew clean copyDocument build --debug
RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]