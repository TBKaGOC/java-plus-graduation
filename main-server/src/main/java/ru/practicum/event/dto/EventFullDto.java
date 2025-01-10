package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    CategoryDto category;
    Boolean paid;
    LocalDateTime eventDate;
    UserDto initiator;
    @Size(min = 1, max = 1024)
    String description;
    Integer participantLimit;
    String state;
    String createdOn;
//    @JsonIgnore
    Location location;
    Boolean requestModeration;
    Long confirmedRequests;
    String publishedOn;
    Integer views;
}
