package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.feign.RequestFeignClient;
import ru.practicum.service.RequestService;

import java.util.Collection;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class FeignRequestController implements RequestFeignClient {

    private final RequestService service;

    @Override
    public List<RequestDto> findRequests(List<Long> ids) {
        log.info("Получение запроса с ID: {} из Сервиса событий", ids);
        return service.findRequests(ids);
    }

    @Override
    public List<RequestDto> updateAllRequest(List<RequestDto> requestDtoList) {
        return service.updateAllRequests(requestDtoList);
    }

    @Override
    public Collection<RequestDto> get(long eventId) {
        return service.getRequestByEventId(eventId);
    }
}
