package ru.practicum.event.model.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.category.model.converter.CategoryToCategoryResponseDto;
import ru.practicum.dto.event.event.EventRequestDto;
import ru.practicum.dto.event.event.Location;
import ru.practicum.event.model.Event;
import ru.practicum.feign.UserFeignClient;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class EventToEventRequestDtoConverter implements Converter<Event, EventRequestDto> {


    private final CategoryToCategoryResponseDto converterCategory;
    private final UserFeignClient userFeignClient;


    @Override
    public EventRequestDto convert(Event source) {
        return EventRequestDto.builder()
                .id(source.getId())
                .title(source.getTitle())
                .confirmedRequests(Long.valueOf(source.getConfirmedRequests()))
                .eventDate(source.getEventDate())
                .annotation(source.getAnnotation())
                .paid(source.getPaid())
                .createdOn(source.getCreatedOn())
                .description(source.getDescription())
                .state(source.getState())
                .participantLimit(source.getParticipantLimit())
                .location(new Location(source.getLocation().getLat(), source.getLocation().getLon()))
                .category(converterCategory.convert(source.getCategory()))
                .initiator(userFeignClient.findShortUsers(Collections.singletonList(source.getInitiator())).getFirst())
                .requestModeration(source.getRequestModeration())
                .views((source.getViews() == null) ? 0L : source.getViews())
                .build();
    }
}
