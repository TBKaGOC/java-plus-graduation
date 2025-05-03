package ru.practicum.compilation.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.api.dto.compilation.ResponseCompilationDto;
import ru.practicum.api.exception.NotFoundException;

import java.util.List;

@RequestMapping("/compilations")
public interface PublicCompilationInterface {
    @GetMapping
    List<ResponseCompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size);

    @GetMapping("/{compId}")
    ResponseCompilationDto getCompilationById(@PathVariable Long compId) throws NotFoundException;
}
