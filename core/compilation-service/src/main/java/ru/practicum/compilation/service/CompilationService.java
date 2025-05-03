package ru.practicum.compilation.service;

import ru.practicum.api.dto.compilation.NewCompilationDto;
import ru.practicum.api.dto.compilation.ResponseCompilationDto;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.api.exception.ValidationException;
import ru.practicum.api.request.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    ResponseCompilationDto addCompilation(NewCompilationDto dto);

    ResponseCompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilation) throws NotFoundException;

    ResponseCompilationDto getCompilationById(Long id) throws NotFoundException;

    List<ResponseCompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    void deleteCompilation(Long id) throws ValidationException, NotFoundException;
}
