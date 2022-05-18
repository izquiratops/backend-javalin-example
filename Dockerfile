#
# Build
#
FROM maven:3.8.1-jdk-11-slim AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

#
# Package with Distroless
# docker build --tag distroless --target distroless .
#
FROM gcr.io/distroless/java11-debian11 AS distroless
COPY --from=build /usr/src/app/target/Server-jar-with-dependencies.jar /usr/app/Server.jar
ENTRYPOINT ["java","-jar","/usr/app/Server.jar"]

#
# Package with JRE 11 Slim
# docker build --tag jre-slim --target jre-slim .
#
FROM openjdk:11-jre-slim AS jre-slim
COPY --from=build /usr/src/app/target/Server-jar-with-dependencies.jar /usr/app/Server.jar
ENTRYPOINT ["java","-jar","/usr/app/Server.jar"]
