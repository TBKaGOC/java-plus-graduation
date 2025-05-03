package ru.practicum.compilation.ui;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.dto.compilation.NewCompilationDto;
import ru.practicum.api.dto.compilation.ResponseCompilationDto;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.api.exception.ValidationException;
import ru.practicum.api.request.compilation.UpdateCompilationRequest;

@RequestMapping("/admin/compilations")
public interface AdminCompilationInterface {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseCompilationDto add(@Valid @RequestBody NewCompilationDto compilationDto);

    @PatchMapping("/{compId}")
    ResponseCompilationDto update(
            @PathVariable Long compId,
            @Valid @RequestBody UpdateCompilationRequest compilationDto
    ) throws NotFoundException;

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long compId) throws ValidationException, NotFoundException;
}
