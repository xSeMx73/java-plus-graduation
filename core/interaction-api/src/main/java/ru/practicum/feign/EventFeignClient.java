package ru.practicum.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.event.event.EventFullResponseDto;
import ru.practicum.dto.event.event.EventRequestDto;


@FeignClient(name = "event-service", path = "/events")
public interface EventFeignClient {
    @GetMapping("/user/{userId}")
    EventFullResponseDto getByInitiator(@PathVariable long userId);

    @GetMapping("/{eventId}/get")
    EventRequestDto getBy(@PathVariable long eventId);

    @PostMapping("/event/{eventId}")
    EventRequestDto updateConfirmRequests(@PathVariable Long eventId, @RequestBody EventRequestDto event);
}
