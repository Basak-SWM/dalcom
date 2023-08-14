FROM gradle:latest

FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar

ARG PROFILE
ENV PROFILE $PROFILE

COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-Dspring.profiles.active=${profile}", "-jar", "/app.jar"]
ENTRYPOINT ["/bin/sh", "-c", "/bin/bash"]