package ru.practicum.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.ResponseCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationServiceImpl implements CompilationService {
    final CompilationRepository repository;

    @Override
    @Transactional
    public ResponseCompilationDto addCompilation(NewCompilationDto dto) {
        return CompilationMapper.mapToResponseCompilation(
                repository.save(CompilationMapper.mapToCompilation(dto))
        );
    }

    @Override
    public ResponseCompilationDto updateCompilation(Compilation compilation) {
        Compilation old = repository.findById(compilation.getId())
                .orElseThrow(() -> new NotFoundException("Указанная подборка не найдена " + compilation.getId()));

        Compilation update = new Compilation();
        update.setId(compilation.getId());
        update.setPinned(compilation.getPinned() == null ? old.getPinned() : compilation.getPinned());
        update.setTitle(compilation.getTitle() == null ? old.getTitle() : compilation.getTitle());
        update.setEvents(compilation.getEvents() == null ? old.getEvents() : compilation.getEvents());

        return CompilationMapper.mapToResponseCompilation(repository.save(update));
    }

    @Override
    public ResponseCompilationDto getCompilationById(Long id) {
        return CompilationMapper.mapToResponseCompilation(repository.findById(id).orElseThrow(
                () -> new NotFoundException("Указанная подборка не найдена " + id)
        ));
    }

    /* todo: 1) Уменьшение числа обращений к базе данных
    **       2) Упрощение логики.
    */
    @Override
    public List<ResponseCompilationDto> getCompilations(Optional<Boolean> pinned, Integer from, Integer size) {
        if (pinned.isPresent()) {
            return repository.findAllWithPinned(pinned.get(), Pageable.ofSize(size + from))
                    .subList(from, size + from)
                    .stream()
                    .map(CompilationMapper::mapToResponseCompilation)
                    .collect(Collectors.toList());
        } else {
            return repository.findAll(Pageable.ofSize(size + from))
                    .map(CompilationMapper::mapToResponseCompilation)
                    .stream()
                    .collect(Collectors.toList())
                    .subList(from, size + from);
        }
    }

    /* todo: 1) Уменьшение числа обращений к базе данных
    **       2) Упрощение логики.
    */
    @Override
    public void deleteCompilation(Long id) {
        Compilation compilation = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Указанная категория не найдена " + id));
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ValidationException("Невозможно удаление используемой категории события " + e.getMessage());
        }
    }
}
