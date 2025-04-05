package ru.practicum.event.model.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.dto.event.event.NewEventDto;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Component
public class NewEventDtoToEvent implements Converter<NewEventDto, Event> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Event convert(NewEventDto source) {
        Event event = new Event();
        event.setAnnotation(source.annotation());
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(source.description());
        event.setEventDate(source.eventDate());
        event.setPaid(source.paid());
        event.setParticipantLimit(source.participantLimit());
        event.setRequestModeration(source.requestModeration());
        event.setTitle(source.title());
        event.setLocation(source.location());
        return event;
    }
}
