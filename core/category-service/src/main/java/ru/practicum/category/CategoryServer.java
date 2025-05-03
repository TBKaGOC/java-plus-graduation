package ru.practicum.category;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.practicum")
public class CategoryServer {
    public static void main(String[] args) {
        SpringApplication.run(CategoryServer.class, args);
    }
}
