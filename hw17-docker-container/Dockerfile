FROM bellsoft/liberica-openjdk-alpine:17
COPY /target/hw17-docker-container-1.0.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]