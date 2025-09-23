# Etapa 1: Construcción con Maven
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app
COPY . .

RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen final ligera con solo el JRE
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para iniciar la app
ENTRYPOINT ["java", "-jar", "app.jar"]