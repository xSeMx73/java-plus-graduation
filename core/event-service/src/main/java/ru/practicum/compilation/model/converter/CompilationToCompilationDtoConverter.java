package ru.practicum.compilation.model.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.dto.event.compilation.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.model.converter.EventToEventShortResponseDtoConverter;

@RequiredArgsConstructor
@Component
public class CompilationToCompilationDtoConverter implements Converter<Compilation, CompilationDto> {
    private final EventToEventShortResponseDtoConverter converter;

    @Override
    public CompilationDto convert(Compilation source) {
        return CompilationDto.builder()
                .id(source.getId())
                .title(source.getTitle())
                .pinned(source.getPinned())
                .events(source.getEvents().stream().map(converter::convert).toList())
                .build();
    }
}
