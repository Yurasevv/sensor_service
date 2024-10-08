FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/sensor-api-0.0.1-SNAPSHOT.jar /app/sensor-api.jar

ENTRYPOINT ["java", "-jar", "/app/sensor-api.jar"]

EXPOSE 8080