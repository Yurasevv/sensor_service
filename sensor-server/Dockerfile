FROM openjdk:17-jdk-alpine

WORKDIR /server

COPY target/sensor-server-0.0.1-SNAPSHOT.jar /server/sensor-server.jar

ENTRYPOINT ["java", "-jar", "/server/sensor-server.jar"]

EXPOSE 8000