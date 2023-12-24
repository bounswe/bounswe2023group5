FROM maven:3.9.6-eclipse-temurin-21-jammy
WORKDIR /app/annotation
COPY . /app/annotation
RUN mvn clean install
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/annotation/target/annotation-1.0-SNAPSHOT.jar"]