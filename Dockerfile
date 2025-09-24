# Etapa 1: Build con Maven
FROM openjdk:17-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen final ligera
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render necesita el puerto expuesto
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
