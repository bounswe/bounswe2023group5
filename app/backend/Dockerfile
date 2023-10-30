FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
VOLUME /app/images
COPY target/*.jar /app/bounswe.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/bounswe.jar"]