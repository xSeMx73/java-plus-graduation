package ru.practicum.service;

import ru.practicum.dto.request.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto create(long userId, long eventId);

    List<RequestDto> getAllRequestByUserId(long userId);

    RequestDto cancel(long userId, long requestId);

    List<RequestDto> findRequests(List<Long> ids);

    List<RequestDto> updateAllRequests(List<RequestDto> requestDtoList);

    List<RequestDto> getRequestByEventId(long eventId);
}
