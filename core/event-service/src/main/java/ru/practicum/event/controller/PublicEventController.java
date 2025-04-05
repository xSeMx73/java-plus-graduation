package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.event.EventFullResponseDto;
import ru.practicum.dto.event.event.EventRequestDto;
import ru.practicum.event.service.EventService;
import ru.practicum.feign.EventFeignClient;

import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController implements EventFeignClient {
    private final EventService eventService;

    @GetMapping
    public List<EventFullResponseDto> publicGetEvents(@RequestParam(required = false) String text,
                                                      @RequestParam(required = false) List<Long> categories,
                                                      @RequestParam(required = false, defaultValue = "false") Boolean paid,
                                                      @RequestParam(required = false) String rangeStart,
                                                      @RequestParam(required = false) String rangeEnd,
                                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                      @RequestParam(required = false, defaultValue = "VIEWS") String sort,
                                                      @RequestParam(defaultValue = "0") Integer from,
                                                      @RequestParam(defaultValue = "10") Integer size,
                                                      HttpServletRequest request) {
        log.info("Получить события, согласно условиям");
        return eventService.publicGetEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    EventFullResponseDto publicGetEvent(@PathVariable Long id,
                            HttpServletRequest request) {
        log.info("Запрос события с ID: {}", id);
        return eventService.publicGetEvent(id, request);
    }

    @Override
    public EventFullResponseDto getByInitiator(long userId) {
        return eventService.getEventByInitiator(userId);
    }

    @Override
    public EventRequestDto getBy(long eventId) {
        return eventService.getEventById(eventId);
    }

    @Override
    public EventRequestDto updateConfirmRequests(Long eventId, EventRequestDto event) {
        log.info("Обновление события с ID: {}", eventId);
        return eventService.updateConfirmRequests(eventId, event);
    }
}