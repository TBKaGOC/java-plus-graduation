package ru.practicum.comment.service;

import ru.practicum.api.dto.comment.CommentDto;
import ru.practicum.api.exception.ConflictException;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.api.request.comment.GetCommentsAdminRequest;

import java.util.List;

public interface CommentService {

    CommentDto addComment(CommentDto commentDto, Long userId, Long eventId) throws NotFoundException, ConflictException;

    void delete(Long userId, Long commentId) throws NotFoundException, ConflictException;

    void delete(Long commentId) throws NotFoundException;

    CommentDto updateUserComment(Long userId, Long commentId, CommentDto commentDto) throws NotFoundException, ConflictException;

    List<CommentDto> getAllUserComments(Long userId) throws NotFoundException;

    List<CommentDto> getAllEventComments(GetCommentsAdminRequest param) throws NotFoundException;

    List<CommentDto> getAllEventComments(Long eventId, int from, int size) throws NotFoundException;

}
