FROM maven:3.8.3-jdk-11 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean install

FROM openjdk:11-jre-slim

COPY --from=build /app/target/supplier_registration-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]