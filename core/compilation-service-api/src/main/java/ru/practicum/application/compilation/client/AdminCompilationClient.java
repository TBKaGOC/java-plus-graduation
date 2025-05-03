package ru.practicum.application.compilation.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.compilation.ui.AdminCompilationInterface;

@FeignClient(name = "compilation-service")
public interface AdminCompilationClient extends AdminCompilationInterface {
}
