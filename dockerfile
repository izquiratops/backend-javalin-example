FROM gcr.io/distroless/java11-debian11
WORKDIR /webserver
COPY target/Server-jar-with-dependencies.jar server.jar
ENTRYPOINT ["java","-jar","server.jar"]
