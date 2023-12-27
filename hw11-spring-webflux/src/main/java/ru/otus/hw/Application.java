package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {
    //TODO: MapStruct вместо своих мапперов
    //TODO: редактирование и создание комментов
    //TODO: @RestControllerAdvice
    //TODO: docker for mongodb/redis
    //
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
