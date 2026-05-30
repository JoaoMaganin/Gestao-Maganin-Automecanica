# Estágio 1: Build (se você não construir o .jar na sua máquina antes)
# FROM maven:3.8.5-openjdk-17 AS build
# COPY src /home/app/src
# COPY pom.xml /home/app
# RUN mvn -f /home/app/pom.xml clean package

# Estágio 2: Imagem final
# Usamos uma imagem enxuta que contém apenas o ambiente de execução Java
FROM openjdk:17-slim

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo .jar da sua máquina (do diretório target) para dentro do container
COPY target/*.jar app.jar

# Expõe a porta que o Spring Boot usa
EXPOSE 8080

# Comando para executar a aplicação quando o container iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]