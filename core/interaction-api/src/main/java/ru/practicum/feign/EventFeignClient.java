package ru.practicum.feign;

import ewm.interaction.dto.eventandadditional.event.EventFullDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-and-additional-service", path = "/events/feign")
public interface EventFeignClient {
    @GetMapping("/{eventId}/{userId}")
    EventFullDto getBy(@PathVariable long userId, @PathVariable long eventId);

    @GetMapping("/{eventId}")
    EventFullDto getBy(@PathVariable long eventId);
}
