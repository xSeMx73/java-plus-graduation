package ru.practicum.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.stat.HitDto;
import ru.practicum.dto.stat.StatDto;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "stats-server")
public interface StatFeignClient {
    @PostMapping("/hit")
    ResponseEntity<HitDto> hit(@Valid @RequestBody HitDto hit);

    @GetMapping("/stats")
    List<StatDto> getStats(@RequestParam("start") LocalDateTime start,
                           @RequestParam("end") LocalDateTime end,
                           @RequestParam("uris") List<String> uris,
                           @RequestParam("unique") boolean unique);

    @GetMapping("/stats/event")
    Long getEventViews(@RequestParam String uri);

}
