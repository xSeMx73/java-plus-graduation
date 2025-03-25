package ru.practicum.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.HitDto;
import ru.practicum.model.Hit;

@Component
public class HitToHitDtoConverter implements Converter<Hit, HitDto> {

    @Override
    public HitDto convert(Hit source) {
        HitDto dto = new HitDto();
        dto.setIp(source.getIp());
        dto.setApp(source.getApp());
        dto.setTimestamp(source.getTimestamp());
        dto.setUri(source.getUri());
        return dto;
    }
}
