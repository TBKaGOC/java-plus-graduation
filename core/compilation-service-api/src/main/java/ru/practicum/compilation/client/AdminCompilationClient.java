package ru.practicum.compilation.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.compilation.ui.AdminCompilationInterface;

@FeignClient(name = "compilation-service")
public interface AdminCompilationClient extends AdminCompilationInterface {
}
