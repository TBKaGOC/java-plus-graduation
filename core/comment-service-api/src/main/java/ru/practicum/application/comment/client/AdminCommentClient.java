package ru.practicum.application.comment.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.comment.ui.AdminCommentInterface;

@FeignClient(name = "comment-service")
public interface AdminCommentClient extends AdminCommentInterface {
}
