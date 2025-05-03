package ru.practicum.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.practicum")
public class EventsServer {
    public static void main(String[] args) {
        SpringApplication.run(EventsServer.class, args);
    }
}
