package ru.practicum.comment.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.comment.ui.AdminCommentInterface;

@FeignClient(name = "comment-service")
public interface AdminCommentClient extends AdminCommentInterface {
}
