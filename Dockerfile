# Используем официальный образ PostgreSQL с Docker Hub
FROM postgres:latest

# Установка переменных среды для PostgreSQL
ENV POSTGRES_DB=postgres
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

# Опционально: установка дополнительных пакетов или настройка PostgreSQL
# RUN apt-get update && apt-get install -y ...

# Опционально: экспонирование порта PostgreSQL
# EXPOSE 5432

# Опционально: команда по умолчанию, которая будет выполняться при запуске контейнера
# CMD ["postgres"]
