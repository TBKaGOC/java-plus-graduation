package ru.practicum.application.comment.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.comment.ui.UserCommentInterface;

@FeignClient(name = "comment-service")
public interface UserCommentClient extends UserCommentInterface {
}
