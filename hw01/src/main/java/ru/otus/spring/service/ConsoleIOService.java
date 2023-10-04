package ru.otus.spring.service;

public class ConsoleIOService implements IOService{

    @Override
    public void println(String message) {
        System.out.println(message);
    }
}
