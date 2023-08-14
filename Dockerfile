FROM gradle:latest

FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar

ENV profile $profile

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${profile}", "-jar", "/app.jar"]
