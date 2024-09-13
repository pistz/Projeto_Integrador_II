FROM ubuntu:22.04 AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y

COPY . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:17-jdk-slim

EXPOSE 8080

# Copia o JAR construído na etapa anterior para o container
COPY --from=build /target/gestao-acolhidos-0.0.1-SNAPSHOT.jar app.jar

# Define o comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
