package ru.practicum.dto.event.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(toBuilder = true)
public record CategoryCreateDto(
        Long id,

        @NotBlank
        String name
) {
}
