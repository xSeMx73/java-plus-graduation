package ru.practicum.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    RequestDto toRequestDto(Request request);
}
