package ru.practicum.comment.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.dto.comment.CommentDto;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.api.request.comment.GetCommentsAdminRequest;
import ru.practicum.comment.service.CommentService;
import ru.practicum.comment.ui.AdminCommentInterface;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCommentController implements AdminCommentInterface {

    private final CommentService service;

    @Override
    public Collection<CommentDto> getComments(Long eventId, Integer from, Integer size) throws NotFoundException {
        return service.getAllEventComments(new GetCommentsAdminRequest(eventId, from, size));
    }

    @Override
    public void removeComment(@PathVariable("commentId") Long commentId) throws NotFoundException {
        log.info("удаление комментария с id {}", commentId);
        service.delete(commentId);
    }

}