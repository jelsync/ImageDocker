# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .

# Descargar dependencias (esta capa se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Construir la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Runtime (imagen liviana)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiar el WAR desde la etapa de build
COPY --from=build /app/target/*.war app.war

# Cambiar permisos
RUN chown -R appuser:appgroup /app

# Cambiar a usuario no-root
USER appuser

# Exponer el puerto
EXPOSE 8080

# Configuración de JVM optimizada para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.war"]

