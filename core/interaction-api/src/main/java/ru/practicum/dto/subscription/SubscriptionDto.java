package ru.practicum.dto.subscription;

import lombok.Data;
import ru.practicum.user.dto.UserShortDto;

import java.util.Set;

@Data
public class SubscriptionDto {
    private Set<UserShortDto> subscribers;
    private Set<UserShortDto> blackList;
}
