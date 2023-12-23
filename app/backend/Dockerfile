FROM maven:3.9.6-eclipse-temurin-21-jammy
WORKDIR /app/backend
VOLUME /app/backend/images
COPY . /app/backend/
RUN mvn clean install
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/backend/target/gamereview-0.0.1-SNAPSHOT.jar"]