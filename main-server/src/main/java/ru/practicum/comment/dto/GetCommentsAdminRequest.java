package ru.practicum.comment.dto;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetCommentsAdminRequest {
    Long eventId;
    Integer from;
    Integer size;
}
