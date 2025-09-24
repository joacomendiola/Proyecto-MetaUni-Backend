# Etapa 1: build con Maven
FROM amazoncorretto:17 as build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Etapa 2: runtime
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
