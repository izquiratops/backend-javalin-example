FROM gcr.io/distroless/java11-debian11
WORKDIR /webserver
COPY target/Server-jar-with-dependencies.jar webserver/server.jar
ENTRYPOINT ["java","-jar","webserver/server.jar"]
