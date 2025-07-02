# Explore With Me
Приложение, позволяющее создавать события и участвовать в них, оставляя комментарии.
## Core
Стек: Java, Spring Boot, Spring Cloud, REST API, Hibernate, Eureka Client, Spring Cloud Config Client, OpenFeign
### user-service
Регистрация и администрирование пользователей
### event-service
Создание и управление событиями, а также из подборками
### request-service
Администрирование заявок пользователей на участие в различных событиях
### category-service
Создание и управление категориями событий 
### comment-service
Регистрирование и управление комментариями у событий
### interection-api
Объекты для взаимодействия по контракту Core
### *-api
Контракт *-service 
 

## Infra
Стек: Java, Spring Cloud, Eureka, Spring Cloud Config, Gateway
### discovery-server
Реестр сервисов
### config-server
Аудит настроек для сервисов
### gateway-server
Единая точка входа для взаимодействия с микросервисами

## Stats
Стек: Java, Spring Boot, Spring Cloud, Apache Kafka, gRPC, Hibernate, Eureka Client, Spring Cloud Config Client
### stats-service
Сервис сбора статистики по событиям
### stats-client
Контракт stats-service
### stats-dto
Объекты для взаимодействия по контракту stats-service

## Setup
### Core
Необходимо: Java 21, запущенные infra/discovery-server, infra/config-server, stats, PostgreSQL на порту 5432 
### Stats
Необходимо: Java 21, запущенные infra/discovery-server, infra/config-server, PostgreSQL на порту 6432, kafka на порту 9092
### Gateway 
Необходимо: Java 21, запущенные infra/discovery-server, infra/config-server Для коректной работы сначала запустить весь Core
