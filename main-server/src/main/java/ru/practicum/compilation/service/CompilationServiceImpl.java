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
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationServiceImpl implements CompilationService {
    final CompilationRepository repository;
    final EventRepository eventRepository;

    @Override
    @Transactional
    public ResponseCompilationDto addCompilation(NewCompilationDto dto) {
        List<Event> events = getEventsFromDto(dto);
        return CompilationMapper.mapToResponseCompilation(
                repository.save(CompilationMapper.mapToCompilation(dto))
        );
    }

    @Override
    public ResponseCompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilation) {
        Compilation old = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Указанная подборка не найдена " + compId));

        Compilation update = new Compilation();
        old.setId(compId);
        old.setPinned(compilation.getPinned() == null ? old.getPinned() : compilation.getPinned());
        old.setTitle(compilation.getTitle() == null ? old.getTitle() : compilation.getTitle());

        List<Event> events = getEventsFromDto(compilation);
        old.setEvents(events == null ? old.getEvents() : events);

        return CompilationMapper.mapToResponseCompilation(repository.save(update));
    }

    private List<Event> getEventsFromDto(NewCompilationDto compilation) {
        List<Event> events = new ArrayList<>();
        if (compilation.getEvents() != null) {
            events = eventRepository.findAllByIdIn(compilation.getEvents());
        }
        return events;
    }

    private List<Event> getEventsFromDto(UpdateCompilationRequest compilation) {
        List<Event> events = new ArrayList<>();
        if (compilation.getEvents() != null) {
            events = eventRepository.findAllByIdIn(compilation.getEvents());
        }
        return events;
    }

    @Override
    public ResponseCompilationDto getCompilationById(Long id) {
        return CompilationMapper.mapToResponseCompilation(repository.findById(id).orElseThrow(
                () -> new NotFoundException("Указанная подборка не найдена " + id)
        ));
    }

    @Override
    public List<ResponseCompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> allWithPinned = repository.findAllWithPinned(pinned, Pageable.ofSize(size + from));
        return allWithPinned
                .stream()
                .map(CompilationMapper::mapToResponseCompilation)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCompilation(Long id) {
        Compilation compilation = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Указанная категория не найдена " + id));
        try {
            repository.delete(compilation);
        } catch (Exception e) {
            throw new ValidationException("Невозможно удаление используемой категории события " + e.getMessage());
        }
    }
}
