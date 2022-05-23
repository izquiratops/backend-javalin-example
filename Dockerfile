# Package with Distroless
# docker build --tag distroless --target distroless .
#
FROM gcr.io/distroless/java11-debian11 AS distroless
COPY target/Server-jar-with-dependencies.jar Server.jar
ENTRYPOINT ["java","-jar","Server.jar"]

# Package with JRE 11 Slim
# docker build --tag jre-slim --target jre-slim .
#
FROM openjdk:11-jre-slim AS slim
COPY target/Server-jar-with-dependencies.jar Server.jar
ENTRYPOINT ["java","-jar","Server.jar"]
