package ru.practicum.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class CompilationServer {
    public static void main(String[] args) {
        SpringApplication.run(CompilationServer.class, args);
    }
}
