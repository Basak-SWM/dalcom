FROM gradle:latest

RUN mkdir ./build
RUN mkdir ./buikd/libs
RUN gradle clean build --no-daemon

FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
