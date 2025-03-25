package ru.practicum.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.HitDto;
import ru.practicum.model.Hit;

@Component
public class HitDtotoHitConverter implements Converter<HitDto, Hit> {

    @Override
    public Hit convert(HitDto source) {
        Hit hit = new Hit();
        hit.setIp(source.getIp());
        hit.setApp(source.getApp());
        hit.setUri(source.getUri());
        hit.setTimestamp(source.getTimestamp());
        return hit;
    }
}
