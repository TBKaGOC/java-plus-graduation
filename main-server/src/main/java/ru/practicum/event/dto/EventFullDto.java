package ru.practicum.event.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.Location;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {

    Long id;
	@Size(min = 1, max = 128)
    String title;
	@Size(min = 1, max = 1024)
    String annotation;
	@Size(min = 1, max = 1024)
    String description;
    String state;
    CategoryDto category;
    Long confirmedRequests;
    String createdOn;
    String publishedOn;
    UserDto initiator;
    Location location;
    LocalDateTime eventDate;
    Boolean paid;
    Boolean requestModeration;
    Integer participantLimit;
    Integer views;
}
