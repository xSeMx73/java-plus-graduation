package ru.practicum.event.service;

import ru.practicum.dto.event.event.*;
import ru.practicum.dto.request.RequestDto;

import java.util.List;

public interface EventService {
    List<EventFullResponseDto> getEvents(Long userId, Integer from, Integer size);

    EventFullResponseDto getEventById(Long userId, Long eventId, String ip, String uri);

    EventFullResponseDto createEvent(Long userId, NewEventDto eventDto);

    List<EventFullResponseDto> adminGetEvents(AdminGetEventRequestDto requestParams);

    EventFullResponseDto adminChangeEvent(Long eventId, UpdateEventUserRequest eventDto);

    EventFullResponseDto updateEvent(Long userId, UpdateEventUserRequest eventDto, Long eventId);

    List<EventFullResponseDto> publicGetEvents(String text,List<Long> categories, Boolean paid, String rangeStart,
                                               String rangeEnd, Boolean onlyAvailable,String sort,Integer from,
                                               Integer size);

    EventFullResponseDto publicGetEvent(Long eventId, Long userId);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    List<RequestDto> getUserRequests(Long userId, Long eventId);

    EventFullResponseDto getEventByInitiator(Long userId);

    EventRequestDto updateConfirmRequests(Long eventId, EventRequestDto event);

    EventRequestDto getEventById(long eventId);

    List<EventFullResponseDto> getRecommendations(long userId, int maxResults);

    void likeEvent(Long eventId, long userId);
}
