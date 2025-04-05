package ru.practicum.dto.user;

import lombok.Builder;

@Builder
public record UserShortDto(
        long id,
        String name) {
}
