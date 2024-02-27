package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class LibraryServiceApplication {
    //TODO: add tests for hystrix
    public static void main(String[] args) {
        SpringApplication.run(LibraryServiceApplication.class, args);
    }
}