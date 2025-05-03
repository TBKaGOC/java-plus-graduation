package ru.practicum.compilation.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.dto.compilation.NewCompilationDto;
import ru.practicum.api.dto.compilation.ResponseCompilationDto;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.api.exception.ValidationException;
import ru.practicum.api.request.compilation.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.compilation.ui.AdminCompilationInterface;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCompilationController implements AdminCompilationInterface {

    final CompilationService compilationService;

    @Override
    public ResponseCompilationDto add(NewCompilationDto compilationDto) {
        return compilationService.addCompilation(compilationDto);
    }

    @Override
    public ResponseCompilationDto update(Long compId, UpdateCompilationRequest compilationDto) throws NotFoundException {
        return compilationService.updateCompilation(compId, compilationDto);
    }

    @Override
    public void delete(Long compId) throws ValidationException, NotFoundException {
        compilationService.deleteCompilation(compId);
    }
}
