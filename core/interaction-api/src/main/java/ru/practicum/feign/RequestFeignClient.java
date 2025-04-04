package ru.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.RequestDto;

import java.util.Collection;
import java.util.List;


@FeignClient(name = "request-service")
public interface RequestFeignClient {

    @GetMapping("request/findRequests")
    List<RequestDto> findRequests(@RequestParam List<Long> ids);

    @PostMapping("/request/all")
    List<RequestDto> updateAllRequest(@RequestBody List<RequestDto> requestDtoList);

    @GetMapping("/request/get/{eventId}")
    Collection<RequestDto> get(@PathVariable final long eventId);
}


