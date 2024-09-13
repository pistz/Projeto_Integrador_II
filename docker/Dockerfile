# Usa uma imagem base do Docker com a capacidade de rodar docker-compose
FROM docker:24.0.6

# Instala o docker-compose no container
RUN apk add --no-cache docker-compose

# Copia os arquivos do docker-compose.yml para dentro do container
COPY ./docker-compose.yml /app/docker-compose.yml

# Define o diretório de trabalho
WORKDIR /app

# Expõe a porta do PostgreSQL (se necessário)
EXPOSE 5432

# Comando para rodar o docker-compose
CMD ["docker-compose", "up"]
