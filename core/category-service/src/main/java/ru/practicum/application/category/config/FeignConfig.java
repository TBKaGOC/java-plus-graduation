package ru.practicum.application.category.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "ru.practicum.application.event.client")
public class FeignConfig {
}
