FROM openjdk:8-jdk-alpine
COPY target/roombooking-0.0.1-SNAPSHOT.jar roombooking-server-1.0.0.jar
ENTRYPOINT ["java","-jar","roombooking-server-1.0.0.jar"]
EXPOSE 8080