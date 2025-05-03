package ru.practicum.application.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.application.api.dto.compilation.NewCompilationDto;
import ru.practicum.application.api.dto.compilation.ResponseCompilationDto;
import ru.practicum.application.api.exception.NotFoundException;
import ru.practicum.application.api.exception.ValidationException;
import ru.practicum.application.api.request.compilation.UpdateCompilationRequest;
import ru.practicum.application.compilation.mapper.CompilationMapper;
import ru.practicum.application.compilation.model.Compilation;
import ru.practicum.application.compilation.model.CompilationEvent;
import ru.practicum.application.compilation.repository.CompilationRepository;
import ru.practicum.application.event.client.EventClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationServiceImpl implements CompilationService {
    final CompilationRepository compilationRepository;
    final EventClient eventClient;

    @Override
    @Transactional
    public ResponseCompilationDto addCompilation(NewCompilationDto dto) {
        Compilation compilation = CompilationMapper.mapToCompilation(dto);
        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }

        ResponseCompilationDto responseCompilationDto = CompilationMapper.mapToResponseCompilation(
                compilationRepository.save(compilation)
        );
        responseCompilationDto.setEvents(eventClient.getShortByIds(compilation.getEvents().stream()
                .map(CompilationEvent::getId).collect(Collectors.toList())));

        return responseCompilationDto;
    }

    @Override
    public ResponseCompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilation) throws NotFoundException {
        Compilation old = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Указанная подборка не найдена " + compId));

        Compilation update = new Compilation();
        update.setId(compId);
        update.setPinned(compilation.getPinned() == null ? old.getPinned() : compilation.getPinned());
        update.setTitle(compilation.getTitle() == null ? old.getTitle() : compilation.getTitle());

        update.setEvents(compilation.getEvents() == null ? old.getEvents() : compilation.getEvents().stream()
                .map(CompilationEvent::new).collect(Collectors.toList()));

        return CompilationMapper.mapToResponseCompilation(compilationRepository.save(update));
    }

    @Override
    public ResponseCompilationDto getCompilationById(Long id) throws NotFoundException {
        log.info("Получение информации о подборке, id={}", id);
        Compilation compilation = compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Подборка не найдена " + id)
        );
        return compileDtoWithEvents(compilation);
    }

    @Override
    public List<ResponseCompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("pinned {}", pinned);
        List<Compilation> allWithPinned = compilationRepository.findAllWithPinned(pinned, Pageable.ofSize(size + from));
        return compileDtosWithEvents(allWithPinned);
    }

    @Override
    public void deleteCompilation(Long id) throws ValidationException, NotFoundException {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Указанная категория не найдена " + id));
        try {
            compilationRepository.delete(compilation);
        } catch (Exception e) {
            throw new ValidationException("Невозможно удаление используемой категории события " + e.getMessage());
        }
    }

    private List<ResponseCompilationDto> compileDtosWithEvents(List<Compilation> compilations) {

        return compilations.stream()
                .map(this::compileDtoWithEvents)
                .collect(Collectors.toList());
    }

    private ResponseCompilationDto compileDtoWithEvents(Compilation compilation) {
        ResponseCompilationDto result = CompilationMapper.mapToResponseCompilation(compilation);
        result.setEvents(eventClient.getShortByIds(compilation.getEvents().stream()
                .map(CompilationEvent::getId).collect(Collectors.toList())));
        return result;
    }
}
