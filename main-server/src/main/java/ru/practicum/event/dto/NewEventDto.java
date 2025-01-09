package ru.practicum.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.model.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {

    @NotBlank
    @Size(min = 1, max = 1024)
    String annotation;
    Long category;
    @Size(min = 1, max = 128)
    String title;
    @NotBlank
    @Size(min = 32)
    String description;
    LocalDateTime eventDate;
    Location location;
    String state;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
}
