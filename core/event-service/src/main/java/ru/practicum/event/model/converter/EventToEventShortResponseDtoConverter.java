package ru.practicum.event.model.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.category.model.converter.CategoryToCategoryResponseDto;
import ru.practicum.dto.event.event.EventShortResponseDto;
import ru.practicum.event.model.Event;
import ru.practicum.feign.UserFeignClient;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class EventToEventShortResponseDtoConverter implements Converter<Event, EventShortResponseDto> {


    private final CategoryToCategoryResponseDto converterCategory;
    private final UserFeignClient userFeignClient;

    @Override
    public EventShortResponseDto convert(Event source) {
        return EventShortResponseDto.builder()
                .id(source.getId())
                .paid(source.getPaid())
                .title(source.getTitle())
                .views(source.getViews())
                .eventDate(source.getEventDate())
                .annotation(source.getAnnotation())
                .category(converterCategory.convert(source.getCategory()))
                .initiator(userFeignClient.findShortUsers(Collections.singletonList(source.getInitiator())).getFirst())
                .views(source.getViews())
                .build();
    }


}
