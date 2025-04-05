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
public class EventShortResponseDto {

        Long id;
        UserShortDto initiator;
        String title;
        CategoryResponseDto category;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime eventDate;

        String annotation;
        Boolean paid;
        Long confirmedRequests;
        Long views;

}
