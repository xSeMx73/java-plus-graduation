package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record EventFullResponseDto(

        Long id,
        UserShortDto initiator,
        String title,
        CategoryResponseDto category,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime eventDate,

        Location location,
        String annotation,
        String description,
        Long participantLimit,
        Boolean paid,
        Boolean requestModeration,
        Long confirmedRequests,
        Long views,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdOn,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime publishedOn,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        EventState state) {

}
