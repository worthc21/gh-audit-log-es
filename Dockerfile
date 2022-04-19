FROM maven:3.6.0-jdk-11-slim AS build
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=build /app/target/gh-audit-log-es-0.0.1-SNAPSHOT.jar /usr/local/app.jar
COPY scripts/wait-for-it.sh /app/wait-for-it.sh
CMD ["java", "-jar", "/usr/local/app.jar"]