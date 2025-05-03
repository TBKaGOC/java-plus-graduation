package ru.practicum.compilation.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.compilation.ui.PublicCompilationInterface;

@FeignClient(name = "compilation-service")
public interface PublicCompilationClient extends PublicCompilationInterface {
}
