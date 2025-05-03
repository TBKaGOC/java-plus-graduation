package ru.practicum.comment.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.comment.ui.CommentInterface;

@FeignClient(name = "comment-service")
public interface CommentClient extends CommentInterface {
}
