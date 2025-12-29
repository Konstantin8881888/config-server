# Config Server - External Configuration

Централизованный сервер конфигураций для микросервисной архитектуры на базе Spring Cloud Config.

## Назначение
Хранение всех конфигураций приложений в одном месте с возможностью:
- Централизованного управления настройками
- Обновления конфигураций без перезапуска приложений
- Разделения конфигов по окружениям (dev, test, prod)
- Version control конфигураций

## Особенности
- Файловое хранилище конфигураций
- Поддержка YAML и Properties форматов
- Интеграция с Eureka для автоматического обнаружения
- REST API для получения конфигураций
- Безопасность через базовую аутентификацию

## Технологии
- Spring Boot 3.5.8
- Spring Cloud Config Server 2025.0.1
- Java 17

---
## Структура конфигураций

    config-repo/
        application.yml # Общие настройки для всех сервисов
        user-service.yml # Специфичные настройки user-service
        notification-service.yml # Специфичные настройки notification-service

---
## Запуск
    mvn spring-boot:run

## Проверка работоспособности
1. Конфигурация user-service: http://localhost:8888/user-service/default
2. Конфигурация notification-service: http://localhost:8888/notification-service/default
3. Все конфигурации: http://localhost:8888/application/default

---
## REST API Endpoints

| Метод | Путь | Описание |
| --- | --- | --- |
| `GET` | `/{application}/{profile}` | Конфигурация для приложения и профиля |
| `GET`  | `/{application}/{profile}/{label}` | Конфигурация с меткой |
| `GET`  | `/{application}-{profile}.yml` | Конфигурация в YAML формате |
| `GET` | `/{application}-{profile}.properties` | Конфигурация в Properties формате |

---
## Интеграция с клиентами
Клиенты подключаются через `bootstrap.yml`:

    spring:
      application:
        name: user-service  # Имя приложения для поиска конфига
      cloud:
        config:
          uri: http://localhost:8888

## Конфигурация

### Настройки отправки на email:
    notification:
      email:
        enabled: ${EMAIL_ENABLED:true}
        mock: ${EMAIL_MOCK:true} # режим мок-отправки email (логирование вместо реальной отправки), false - реальная отправка.

    spring:
      mail:
        host: ${SMTP_HOST:smtp.gmail.com}
        port: ${SMTP_PORT:587}
        username: ${SMTP_USERNAME:} # адрес SMTP-сервера (только для реальной отправки).
        password: ${SMTP_PASSWORD:} # пароль SMTP-сервера (только для реальной отправки).
        properties:
          mail.smtp.auth: true
          mail.smtp.starttls.enable: true
        default-encoding: UTF-8
        from: ${SMTP_USERNAME:}

### Режим работы с email:
Используется, в зависимости от настроек в notification-service.yml (см. выше в основных настройка), мок-отправка email - все письма логируются в консоль вместо реальной отправки или реальная отправка на электронную почту пользователя.

---