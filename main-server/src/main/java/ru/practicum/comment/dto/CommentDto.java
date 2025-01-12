package ru.practicum.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import static ru.practicum.util.JsonFormatPattern.JSON_FORMAT_PATTERN_FOR_TIME;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CommentDto {
    private Long id;
    private Long userId;
    private Long eventId;
    @JsonProperty("isInitiator")
    private boolean isInitiator;
    @Size(min = 1, max = 5000)
    @NotBlank
    private String content;
    @JsonFormat(pattern = JSON_FORMAT_PATTERN_FOR_TIME)
    private LocalDateTime created;
}
