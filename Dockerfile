# Imagen oficial de Maven con JDK 17
FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app
COPY . .

# Compila y empaqueta
RUN mvn clean package -DskipTests

# Render necesita el puerto expuesto
EXPOSE 8080

# Arranca la app
CMD ["java", "-jar", "target/proyecto6-metauni-0.0.1-SNAPSHOT.jar"]
