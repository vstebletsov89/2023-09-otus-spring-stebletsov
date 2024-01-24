package ru.otus.hw.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.NotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<String> notFoundError(NotFoundException exception) {
        log.info("notFound exception: " + exception.getMessage());

        return new ResponseEntity<>(
                exception.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> internalError(Exception exception) {
        log.info("internalError exception: " + exception.getMessage());
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);

        return new ResponseEntity<>(
                stringWriter.toString(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}