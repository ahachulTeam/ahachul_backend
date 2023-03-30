FROM openjdk:17.0.1-jdk-slim
ARG JAR_FILE=ahachul_backend/build/libs/ahachul_backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/app.jar"]