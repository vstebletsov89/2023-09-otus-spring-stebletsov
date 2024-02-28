package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;


@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
public class LibraryServiceApplication {
    //TODO: add tests for hystrix
    public static void main(String[] args) {
        SpringApplication.run(LibraryServiceApplication.class, args);
    }
}
