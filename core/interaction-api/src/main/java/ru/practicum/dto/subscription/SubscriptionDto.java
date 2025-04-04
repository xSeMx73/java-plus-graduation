package ru.practicum.dto.subscription;

import lombok.Data;
import ru.practicum.dto.user.UserShortDto;

import java.util.Set;

@Data
public class SubscriptionDto {
    private Set<UserShortDto> subscribers;
    private Set<UserShortDto> blackList;
}
