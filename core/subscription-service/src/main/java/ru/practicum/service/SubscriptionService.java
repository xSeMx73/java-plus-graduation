package ru.practicum.service;

import ru.practicum.dto.event.event.EventFullResponseDto;
import ru.practicum.dto.subscription.SubscriptionDto;
import ru.practicum.model.BlackList;
import ru.practicum.model.Subscriber;

import java.util.List;

public interface SubscriptionService {
    void addSubscriber(Subscriber subscriber);

    void addBlacklist(BlackList blackList);

    void removeSubscriber(Long userId, Long subscriberId);

    SubscriptionDto getSubscribers(long userId);

    SubscriptionDto getBlacklists(long userId);

    List<EventFullResponseDto> getEvents(long userId);

    void removeFromBlackList(long userId, long blackListId);
}
