package ru.practicum.dto.event.category;

import lombok.Builder;

@Builder
public record CategoryResponseDto(
        Long id,
        String name
) {
}
