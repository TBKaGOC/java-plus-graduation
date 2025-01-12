package ru.practicum.comment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCommentsAdminRequest {
    private Long eventId;
    private Integer from;
    private Integer size;
}
