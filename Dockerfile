FROM eclipse-temurin:26-jre-alpine

WORKDIR /app
COPY target/IT_M_JAVA_Exchange_Currency-1.0-SNAPSHOT.jar app.jar

RUN mkdir -p /app/database

ENTRYPOINT ["java", "-jar", "app.jar"]
