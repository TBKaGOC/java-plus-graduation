package ru.practicum.application.compilation.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "ru.practicum.client")
public class FeignConfig {
}
