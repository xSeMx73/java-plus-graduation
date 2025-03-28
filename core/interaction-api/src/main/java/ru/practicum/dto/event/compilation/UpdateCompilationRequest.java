package ru.practicum.dto.event.compilation;

import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateCompilationRequest(
        Set<Long> events,
        boolean pinned,
        @Size(max = 50)
        String title
) {
}
