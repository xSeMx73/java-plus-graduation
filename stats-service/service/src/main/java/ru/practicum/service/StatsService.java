package ru.practicum.service;

import org.springframework.stereotype.Repository;
import ru.practicum.HitDto;
import ru.practicum.StatDto;
import ru.practicum.StatRequestDto;

import java.util.List;

@Repository
public interface StatsService {

    HitDto hit(HitDto hitDto);

    List<StatDto> getStats(StatRequestDto statDto);

    Long getEventViews(String uri);
}
