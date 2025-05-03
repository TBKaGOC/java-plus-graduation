package ru.practicum.comment.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.comment.ui.UserCommentInterface;

@FeignClient(name = "comment-service")
public interface UserCommentClient extends UserCommentInterface {
}
