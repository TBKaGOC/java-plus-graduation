package ru.practicum.application.comment.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.comment.ui.CommentInterface;

@FeignClient(name = "comment-service")
public interface CommentClient extends CommentInterface {
}
