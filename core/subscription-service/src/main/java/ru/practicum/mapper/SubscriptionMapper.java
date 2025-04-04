package ru.practicum.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import ru.practicum.dto.subscription.SubscriptionDto;
import ru.practicum.feign.UserFeignClient;
import ru.practicum.model.BlackList;
import ru.practicum.model.Subscriber;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {

    @Qualifier("conversionService")
    private final ConversionService converter;
    private final UserFeignClient userService;

    public SubscriptionDto subscribertoSubscriptionDto(List<Subscriber> subscriber) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setSubscribers(subscriber.stream()
                .map(Subscriber::getSubscriber)
                .map(s -> userService.findShortUsers(Collections.singletonList(s)))
                .flatMap(List::stream)
                .collect(Collectors.toSet())
        );
        return dto;
    }

    public SubscriptionDto blackListSubscriptionDto(List<BlackList> blackList) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setBlackList(blackList.stream()
                .map(BlackList::getBlackList)
                .map(s -> userService.findShortUsers(Collections.singletonList(s)))
                .flatMap(List::stream)
                .collect(Collectors.toSet())
        );
        return dto;
    }
}
