package ru.practicum.event.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.event.dto.EventShortResponseDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserShortDto;

@RequiredArgsConstructor
@Component
public class EventToEventShortResponseDtoConverter implements Converter<Event, EventShortResponseDto> {


    @Qualifier("conversionService")
    private final ConversionService converter;

    @Override
    public EventShortResponseDto convert(Event source) {
        return EventShortResponseDto.builder()
                .id(source.getId())
                .paid(source.getPaid())
                .title(source.getTitle())
                .views(source.getViews())
                .eventDate(source.getEventDate())
                .annotation(source.getAnnotation())
                .initiator(converter.convert(source.getInitiator(), UserShortDto.class))
                .category(converter.convert(source.getCategory(), CategoryResponseDto.class))
                .views(source.getViews())
                .build();
    }


}
