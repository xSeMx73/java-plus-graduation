package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.event.EventRequestDto;
import ru.practicum.dto.event.event.EventState;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestState;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.NotPossibleException;
import ru.practicum.feign.EventFeignClient;
import ru.practicum.feign.UserFeignClient;
import ru.practicum.model.Request;
import ru.practicum.repository.RequestRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final UserFeignClient userFeignClient;
    private final EventFeignClient eventFeignClient;

    @Qualifier("conversionService")
    private final ConversionService converter;

    @Override
    public RequestDto create(long userId, long eventId) {
        if (!repository.findAllByRequesterAndEventAndStatusNotLike(userId, eventId,
                RequestState.CANCELED).isEmpty())
            throw new NotPossibleException("Request already exists");

        EventRequestDto event = eventFeignClient.getBy(eventId);
        if (userId == event.getInitiator().id())
            throw new NotPossibleException("User is Initiator of event");
        if (!event.getState().equals(EventState.PUBLISHED))
            throw new NotPossibleException("Event is not published");
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit())
            throw new NotPossibleException("Request limit exceeded");
        Request newRequest = new Request();
        newRequest.setRequester(userId);
        newRequest.setEvent(eventId);
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            newRequest.setStatus(RequestState.PENDING);
        } else {
            newRequest.setStatus(RequestState.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventFeignClient.updateConfirmRequests(eventId, event);
        }
        return converter.convert(repository.save(newRequest), RequestDto.class);
    }

    @Override
    public List<RequestDto> getAllRequestByUserId(long userId) {
        userFeignClient.findShortUsers(Collections.singletonList(userId));
        return repository.findAllByRequester(userId).stream()
                .map(r -> converter.convert(r, RequestDto.class))
                .toList();
    }

    @Override
    public List<RequestDto> getRequestByEventId(long eventId) {

        return repository.findAllByEvent(eventId).stream()
                .map(r -> converter.convert(r, RequestDto.class))
                .toList();
    }


    @Override
    @Transactional
    public RequestDto cancel(final long userId, final long requestId) {
        userFeignClient.findShortUsers(Collections.singletonList(userId));
        Request request = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Not found for request id" + requestId));
        if (!request.getRequester().equals(userId))
            throw new NotPossibleException("Request is not by user");
        request.setStatus(RequestState.CANCELED);
        return converter.convert(repository.save(request), RequestDto.class);
    }

    @Override
    public List<RequestDto> findRequests(List<Long> ids) {
        List<Request> requests = repository.findAllById(ids);
        return requests.stream()
                .map(requestDto ->  converter.convert(requestDto, RequestDto.class))
                .toList();

    }

    public List<RequestDto> updateAllRequests(List<RequestDto> requestDtoList) {
        List<Request> list = new ArrayList<>(requestDtoList.size());
        for (RequestDto r : requestDtoList) {
            list.add(converter.convert(r, Request.class));
        }
        return repository.saveAll(list).stream().map(d -> converter.convert(d, RequestDto.class)).toList();
    }
}
