FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY build/libs/PR6automatizationUstynov.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]