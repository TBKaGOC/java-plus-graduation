package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.dto.comment.CommentDto;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.comment.service.CommentService;
import ru.practicum.comment.ui.CommentInterface;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
public class CommentController implements CommentInterface {

    private final CommentService service;

    @Override
    public Collection<CommentDto> getByEvent(Long eventId, Integer from, Integer size) throws NotFoundException {
        return service.getAllEventComments(eventId, from, size);
    }
}

