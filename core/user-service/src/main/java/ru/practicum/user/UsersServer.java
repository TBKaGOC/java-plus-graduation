package ru.practicum.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.practicum")
public class UsersServer {
    public static void main(String[] args) {
        SpringApplication.run(UsersServer.class, args);
    }
}
