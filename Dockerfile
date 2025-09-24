# Etapa 1: Build con Maven (usamos Eclipse Temurin JDK 17)
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen final ligera (solo JRE)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render necesita el puerto expuesto
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
