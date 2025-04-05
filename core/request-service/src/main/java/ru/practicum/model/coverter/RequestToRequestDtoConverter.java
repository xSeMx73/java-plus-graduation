package ru.practicum.model.coverter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.model.Request;

@Component
public class RequestToRequestDtoConverter implements Converter<Request, RequestDto> {

    @Override
    public RequestDto convert(Request source) {
      return  RequestDto.builder()
                .id(source.getId())
                .requester(source.getRequester())
                .event(source.getEvent())
                .created(source.getCreated())
                .status(source.getStatus())
                .build();

    }
}
