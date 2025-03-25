package ru.practicum.compilation.dto.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;

@RequiredArgsConstructor
@Component
public class NewCompilationDtoToCompilationConverter implements Converter<NewCompilationDto, Compilation> {

    @Override
    public Compilation convert(NewCompilationDto source) {
        return Compilation.builder().pinned(source.pinned()).title(source.title()).build();
    }
}
