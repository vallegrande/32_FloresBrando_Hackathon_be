FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x ./mvnw && ./mvnw -B package -DskipTests

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=0 /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
