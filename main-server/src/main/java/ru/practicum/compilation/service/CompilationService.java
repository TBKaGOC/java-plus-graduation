package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.ResponseCompilationDto;
import ru.practicum.compilation.model.Compilation;

import java.util.List;
import java.util.Optional;

public interface CompilationService {
    ResponseCompilationDto addCompilation(NewCompilationDto dto);

    ResponseCompilationDto updateCompilation(Compilation compilation);

    ResponseCompilationDto getCompilationById(Long id);

    List<ResponseCompilationDto> getCompilations(Optional<Boolean> pinned, Integer from, Integer size);

    void deleteCompilation(Long id);
}
