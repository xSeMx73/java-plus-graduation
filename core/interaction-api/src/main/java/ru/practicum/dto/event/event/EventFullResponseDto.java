package ru.practicum.dto.event.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.dto.event.category.CategoryResponseDto;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
public class EventFullResponseDto {

        Long id;
        UserShortDto initiator;
        String title;
        CategoryResponseDto category;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime eventDate;
        Location location;
        String annotation;
        String description;
        Long participantLimit;
        Boolean paid;
        Boolean requestModeration;
        Long confirmedRequests;
        Long views;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdOn;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss")
        LocalDateTime publishedOn;
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        EventState state;

}
