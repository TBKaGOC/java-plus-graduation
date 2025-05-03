package ru.practicum.application.compilation.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.compilation.ui.PublicCompilationInterface;

@FeignClient(name = "compilation-service")
public interface PublicCompilationClient extends PublicCompilationInterface {
}
