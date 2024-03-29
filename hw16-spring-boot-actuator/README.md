# Домашнее задание №16
Использовать метрики, healthchecks и logfile

## Цель:
Реализовать production-grade мониторинг и прозрачность в приложении.
Результат: приложение с применением Spring Boot Actuator.

## Описание/Пошаговая инструкция выполнения домашнего задания:

Данное задание выполняется на основе одного из реализованных Web-приложений.

Требования:
* подключить Spring Boot Actuator в приложение
* включить метрики, healthchecks и logfile
* реализовать свой собственный HealtCheck индикатор
* UI для данных от Spring Boot Actuator реализовывать не нужно
* Опционально: переписать приложение на HATEOAS принципах с помощью Spring
Data REST Repository

Примечание:

**Только admin имеет доступ к actuator и datarest endpoints.**
* тестовый пользователь (ROLE_USER)  "username: user  password: pass"
* тестовый пользователь (ROLE_ADMIN) "username: admin password: pass"
* тестовый пользователь (ROLE_GUEST) "username: guest password: pass"