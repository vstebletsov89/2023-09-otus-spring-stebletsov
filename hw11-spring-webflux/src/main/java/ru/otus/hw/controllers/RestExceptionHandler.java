package ru.otus.hw.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.NotFoundException;

import static org.springframework.http.ResponseEntity.notFound;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity notFoundError(NotFoundException exception) {
        log.info("notFound exception: " + exception.getMessage());
        return notFound().build();
    }
}