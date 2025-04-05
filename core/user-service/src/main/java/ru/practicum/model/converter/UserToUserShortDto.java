package ru.practicum.model.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.User;

@Component
public class UserToUserShortDto implements Converter<User, UserShortDto> {

    @Override
    public UserShortDto convert(User source) {
       return UserShortDto.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }
}
