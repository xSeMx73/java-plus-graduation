package ru.practicum.service;

import org.springframework.stereotype.Repository;
import ru.practicum.dto.stat.HitDto;
import ru.practicum.dto.stat.StatDto;
import ru.practicum.dto.stat.StatRequestDto;

import java.util.List;

@Repository
public interface StatsService {

    HitDto hit(HitDto hitDto);

    List<StatDto> getStats(StatRequestDto statDto);

    Long getEventViews(String uri);
}
