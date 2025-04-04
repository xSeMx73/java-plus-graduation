package ru.practicum.dto.event.compilation;

import lombok.Builder;
import ru.practicum.dto.event.event.EventShortResponseDto;

import java.util.Collection;


@Builder
public record CompilationDto(
       Collection<EventShortResponseDto> events,
        Long id,
        boolean pinned,
        String title) {
}
