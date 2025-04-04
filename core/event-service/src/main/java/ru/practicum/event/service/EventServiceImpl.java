package ru.practicum.event.service;

import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.dto.event.event.*;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestState;
import ru.practicum.dto.stat.HitDto;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.converter.EventToEventFullResponseDtoConverter;
import ru.practicum.exception.*;
import ru.practicum.feign.RequestFeignClient;
import ru.practicum.feign.StatFeignClient;
import ru.practicum.feign.UserFeignClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final CategoryRepository categoryRepository;

    @Qualifier("conversionService")
    private final ConversionService converter;
    private final EventToEventFullResponseDtoConverter listConverter;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String EVENT_NOT_FOUND_MESSAGE = "Event not found";
    private final StatFeignClient statFeignClient;
    private final RequestFeignClient requestFeignClient;
    private final UserFeignClient userFeignClient;

    @Override
    public List<EventFullResponseDto> getEvents(Long userId, Integer from, Integer size) {
        userFeignClient.findShortUsers(Collections.singletonList(userId));
        Pageable pageable = PageRequest.of(from, size);
        List<Event> respEvent = repository.findByInitiator(userId, pageable);
        return listConverter.convertList(respEvent);
    }

    @Override
    public EventFullResponseDto getEventById(Long userId, Long id, String ip, String uri) {
        userFeignClient.findShortUsers(Collections.singletonList(userId));
        Event event = repository.findByIdAndInitiator(id, userId).orElseThrow(() ->
                new NotFoundException(EVENT_NOT_FOUND_MESSAGE));

        return converter.convert(event, EventFullResponseDto.class);
    }

    @Override
    public EventFullResponseDto createEvent(Long userId, NewEventDto eventDto) {

        Category category = getCategory(eventDto.category());
        Event event = converter.convert(eventDto, Event.class);
        if (event == null) throw new NoResultException("Не удалось создать событие");
        event.setInitiator(userId);
        event.setCategory(category);
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0L);
        event.setViews(0L);
        event = repository.save(event);
        event.setCreatedOn(LocalDateTime.now());
        return converter.convert(event, EventFullResponseDto.class);
    }

    @Override
    public EventFullResponseDto updateEvent(Long userId, UpdateEventUserRequest eventDto, Long eventId) {
        userFeignClient.findShortUsers(Collections.singletonList(userId));
        Optional<Event> eventOptional = repository.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new NotFoundException(EVENT_NOT_FOUND_MESSAGE);
        }
        Event foundEvent = eventOptional.get();
        if (foundEvent.getState() == EventState.PUBLISHED) {
            throw new NotPossibleException("Нельзя изменять сообщение, которое опубликовано");
        }
        Event saved = repository.save(updateEventFields(eventDto, foundEvent));
        return converter.convert(saved, EventFullResponseDto.class);
    }

    @Override
    public List<EventFullResponseDto> publicGetEvents(String text,List<Long> categories, Boolean paid, String rangeStart,
                                                      String rangeEnd, Boolean onlyAvailable,String sort,Integer from,
                                                      Integer size, HttpServletRequest request) {

        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart, FORMATTER) : null;
        LocalDateTime end = rangeEnd != null ? LocalDateTime.parse(rangeEnd, FORMATTER) : null;
        if (start != null && end != null) {
            if (start.isAfter(end))
                throw new BadRequestException("Дата окончания, должна быть позже даты старта.");
        }
        List<Event> events = repository.findEventsPublic(
                text,
                categories,
                paid,
                start,
                end,
                EventState.PUBLISHED,
                onlyAvailable,
                PageRequest.of(from,
                        size)
        );
log.info("Метод publicGetEvents вебклиент: {}", statFeignClient.toString());
        hit(request.getRemoteAddr(), request.getRequestURI());

        return listConverter.convertList(events);
    }

    @Override
    public EventFullResponseDto publicGetEvent(Long id, HttpServletRequest request) {
        Event event = getEvent(id);

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Событие c ID: " + id + " не найдено");
        }
        hit(request.getRemoteAddr(),request.getRequestURI());


        Long views = statFeignClient.getEventViews(request.getRequestURI());
        if (views != null) {
            event.setViews(views);
        }
        event = repository.save(event);
        return converter.convert(event, EventFullResponseDto.class);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        userFeignClient.findShortUsers(Collections.singletonList(userId));
        Event event = getEvent(eventId);
        EventRequestStatusUpdateResult response = new EventRequestStatusUpdateResult();
        List<RequestDto> requests = requestFeignClient.findRequests(request.getRequestIds());
        if (request.getStatus().equals(RequestState.REJECTED)) {
            checkRequestsStatus(requests);
            requests.forEach(r -> r.setStatus(RequestState.REJECTED));



            requestFeignClient.updateAllRequest(requests);
            response.setRejectedRequests(requests);
        } else {
            if (requests.size() + event.getConfirmedRequests() > event.getParticipantLimit())
                throw new ConflictException("Превышен лимит заявок");
            requests.forEach(r -> r.setStatus(RequestState.CONFIRMED));
            requestFeignClient.updateAllRequest(requests);
            event.setConfirmedRequests(event.getConfirmedRequests() + requests.size());
            repository.save(event);
            response.setConfirmedRequests(requests);
        }
        return response;
    }

    @Override
    public List<RequestDto> getUserRequests(Long userId, Long eventId) {
       List<RequestDto> requests = new ArrayList<>(requestFeignClient.get(eventId));
        if (requests.isEmpty()) return new ArrayList<>();
        return requests.stream().map(r -> converter.convert(r, RequestDto.class)).collect(Collectors.toList());
    }

    @Override
    public EventFullResponseDto getEventByInitiator(Long userId) {
        return converter.convert(repository.findByInitiator(userId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено")), EventFullResponseDto.class);
    }

    @Override
    public EventRequestDto updateConfirmRequests(Long eventId, EventRequestDto eventDto) {
        Event event = getEvent(eventId);
        event.setConfirmedRequests(eventDto.getConfirmedRequests());
        repository.save(event);
        return converter.convert(event, EventRequestDto.class);
    }

    @Override
    public EventRequestDto getEventById(long eventId) {
        return converter.convert(repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено")), EventRequestDto.class);
    }

    @Override
    public List<EventFullResponseDto> adminGetEvents(AdminGetEventRequestDto requestParams) {
        int from = (requestParams.from() != null) ? requestParams.from() : 0;
        int size = (requestParams.size() != null) ? requestParams.size() : 10;
        LocalDateTime startTime = requestParams.rangeStart() != null ? LocalDateTime.parse(requestParams.rangeStart(), FORMATTER) : null;
        LocalDateTime endTime = requestParams.rangeEnd() != null ? LocalDateTime.parse(requestParams.rangeEnd(), FORMATTER) : null;
        List<Event> events = repository.findEventsByAdmin(
                requestParams.users(),
                requestParams.states(),
                requestParams.categories(),
                startTime,
                endTime,
                PageRequest.of(from / size,
                        size)
        );
        return listConverter.convertList(events);
    }

    @Override
    public EventFullResponseDto adminChangeEvent(Long eventId, UpdateEventUserRequest eventDto) {
        Event event = getEvent(eventId);
        checkEventForUpdate(event, eventDto.stateAction());
        Event updatedEvent = repository.save(prepareEventForUpdate(event, eventDto));
        return converter.convert(updatedEvent, EventFullResponseDto.class);
    }

    private Event getEvent(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND_MESSAGE));
    }

    private void checkEventForUpdate(Event event, StateAction action) {
        checkEventDate(event.getEventDate());
        if (action == null) return;
        if (action.equals(StateAction.PUBLISH_EVENT)
                && !event.getState().equals(EventState.PENDING))
            throw new ParameterConflictException("Events", "Опубликовать событие можно в статусе PENDING, а статус = "
                                                           + event.getState());
        if (action.equals(StateAction.REJECT_EVENT)
                && event.getState().equals(EventState.PUBLISHED))
            throw new ParameterConflictException("Events", "Отменить событие можно только в статусе PUBLISHED, а статус = "
                    + event.getState());
    }

    private Event prepareEventForUpdate(Event event, UpdateEventUserRequest updateEventDto) {
        if (updateEventDto.annotation() != null)
            event.setAnnotation(updateEventDto.annotation());
        if (updateEventDto.description() != null)
            event.setDescription(updateEventDto.description());
        if (updateEventDto.eventDate() != null) {
            checkEventDate(updateEventDto.eventDate());
            event.setEventDate(updateEventDto.eventDate());
        }
        if (updateEventDto.paid() != null)
            event.setPaid(updateEventDto.paid());
        if (updateEventDto.participantLimit() != null)
            event.setParticipantLimit(updateEventDto.participantLimit());
        if (updateEventDto.title() != null)
            event.setTitle(updateEventDto.title());
        if (updateEventDto.stateAction() != null) {
            switch (updateEventDto.stateAction()) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case CANCEL_REVIEW, REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
            }
        }
        return event;
    }

    private void checkEventDate(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now().plusHours(1)))
            throw new ConflictException("Дата начала события меньше чем час " + dateTime);
    }

    private Category getCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new NotFoundException("Категория c ID: " + categoryId + " не найдена");
        }
        return category.get();
    }


    private Event updateEventFields(UpdateEventUserRequest eventDto, Event foundEvent) {
        if (eventDto.category() != null) {
            Category category = getCategory(eventDto.category());
            foundEvent.setCategory(category);
        }

        if (eventDto.annotation() != null && !eventDto.annotation().isBlank()) {
            foundEvent.setAnnotation(eventDto.annotation());
        }
        if (eventDto.description() != null && !eventDto.description().isBlank()) {
            foundEvent.setDescription(eventDto.description());
        }
        if (eventDto.eventDate() != null) {
            if (eventDto.eventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConflictException("Дата начала события не может быть раньше чем через 2 часа");
            }
            foundEvent.setEventDate(eventDto.eventDate());
        }
        if (eventDto.paid() != null) {
            foundEvent.setPaid(eventDto.paid());
        }
        if (eventDto.participantLimit() != null) {
            if (eventDto.participantLimit() < 0) {
                throw new ValidationException("Participant limit cannot be negative");
            }
            foundEvent.setParticipantLimit(eventDto.participantLimit());
        }
        if (eventDto.requestModeration() != null) {
            foundEvent.setRequestModeration(eventDto.requestModeration());
        }
        if (eventDto.title() != null && !eventDto.title().isBlank()) {
            foundEvent.setTitle(eventDto.title());
        }
        if (eventDto.location() != null) {
            if (eventDto.location().getLat() != null) {
                foundEvent.getLocation().setLat(eventDto.location().getLat());
            }
            if (eventDto.location().getLon() != null) {
                foundEvent.getLocation().setLon(eventDto.location().getLon());
            }
        }

        if (eventDto.stateAction() != null) {
            switch (eventDto.stateAction()) {
                case CANCEL_REVIEW -> foundEvent.setState(EventState.CANCELED);
                case PUBLISH_EVENT -> foundEvent.setState(EventState.PUBLISHED);
                case SEND_TO_REVIEW -> foundEvent.setState(EventState.PENDING);
            }
        }
        return foundEvent;
    }

    private void checkRequestsStatus(List<RequestDto> requests) {
        Optional<RequestDto> confirmedReq = requests.stream()
                .filter(request -> request.getStatus().equals(RequestState.CONFIRMED))
                .findFirst();
        if (confirmedReq.isPresent())
            throw new ConflictException("Нельзя отменить, уже принятую заявку.");
    }


    private List<String> getListOfUri(List<Event> events, String uri) {
        return events.stream().map(Event::getId).map(id -> getUriForEvent(uri, id))
                .collect(Collectors.toList());
    }

    private String getUriForEvent(String uri, Long eventId) {
        return uri + "/" + eventId;
    }

    private void hit(String ip, String uri) {
        HitDto hit = new HitDto("ewm-main", uri, ip, LocalDateTime.now());
        statFeignClient.hit(hit);
    }
}
