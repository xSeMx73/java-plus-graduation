package ru.practicum.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.StatDto;
import ru.practicum.StatRequestDto;
import ru.practicum.model.Hit;
import ru.practicum.repository.StatsRepository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {


    private final StatsRepository statsRepository;


    @Qualifier("conversionService")
    private final ConversionService converter;

    @Override
    public HitDto hit(HitDto hitDto) {
        return converter.convert(statsRepository.save(Objects.requireNonNull(
                converter.convert(hitDto, Hit.class))),HitDto.class);
    }

    @Override
    public List<StatDto> getStats(StatRequestDto statDto) {
        if (statDto.getUnique()) {
            return statsRepository.getWithUniqueIpCount(statDto.getStart(), statDto.getEnd(), statDto.getUri());
        } else {
            return statsRepository.getWithIpCount(statDto.getStart(), statDto.getEnd(), statDto.getUri());
        }
    }

    @Override
    public Long getEventViews(String uri) {
        return statsRepository.viewsCount(uri);
    }
}