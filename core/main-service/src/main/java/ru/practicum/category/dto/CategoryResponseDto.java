package ru.practicum.category.dto;

import lombok.Builder;

@Builder
public record CategoryResponseDto(
        Long id,
        String name
) {
}
