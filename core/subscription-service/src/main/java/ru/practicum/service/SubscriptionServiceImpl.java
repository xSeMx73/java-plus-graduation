package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.event.EventFullResponseDto;
import ru.practicum.dto.event.event.EventState;
import ru.practicum.dto.subscription.SubscriptionDto;
import ru.practicum.exception.ConditionsNotMetException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.feign.EventFeignClient;
import ru.practicum.feign.UserFeignClient;
import ru.practicum.mapper.SubscriptionMapper;
import ru.practicum.model.BlackList;
import ru.practicum.model.Subscriber;
import ru.practicum.repository.BlackListRepository;
import ru.practicum.repository.SubscriberRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriberRepository subscriberRepository;
    private final BlackListRepository blackListRepository;
    private final UserFeignClient userFeignClient;
    private final EventFeignClient eventFeignClient;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public void addSubscriber(Subscriber subscriber) {
        log.debug("Проверка пользователя на существование в БД {}", subscriber.getUserId());
        getUser(subscriber.getUserId(), subscriber.getSubscriber());
        checkUserBD(subscriber.getUserId(), subscriber.getSubscriber());
        log.info("POST Запрос Сохранение пользователя в подписчиках {} {}", subscriber.getUserId(), subscriber.getSubscriber());
        subscriberRepository.save(subscriber);
    }

    @Override
    public void addBlacklist(BlackList blackList) {
        checkUserBD(blackList.getUserId(), blackList.getBlackList());
        log.info("POST Запрос Сохранение пользователя в черный список {} {}", blackList.getUserId(), blackList.getBlackList());
        blackListRepository.save(blackList);
    }

    @Override
    public void removeSubscriber(Long userId, Long subscriberId) {
        log.debug("Проверка пользователя на существование в БД {}", userId);
        getUser(userId, subscriberId);
        Optional<Subscriber> subscribed = subscriberRepository.findByUserIdAndSubscriber(userId, subscriberId);
       if (subscribed.isPresent()) {
           subscriberRepository.delete(subscribed.orElseThrow(() -> new NotFoundException("Пользователя нет в подписчиках")));
            log.info("DELETE Запрос на удаление пользователя из подписок выполнено");
        }
    }


    @Override
    public void removeFromBlackList(long userId, long blackListId) {
        log.debug("Проверка пользователей на существование в БД {}", userId);
        getUser(userId,blackListId);
        Optional<BlackList> blackLists = blackListRepository.findByUserIdAndBlockUser(userId, blackListId);
        if (blackLists.isPresent()) {
            blackListRepository.delete(blackLists.orElseThrow(() -> new NotFoundException("Пользователя нет в черном листе")));
            log.info("DELETE Запрос на удаление пользователя из черного списка выполнено");
        }
    }

    @Override
    public SubscriptionDto getSubscribers(long userId) {
        log.debug("Получение списка ID пользователей на которых подписаны");
        List<Subscriber> subscriptions = subscriberRepository.findAllByUserId(userId);
        log.info("GET Запрос на получение списка подписок пользователя выполнен {}", subscriptions);
        return subscriptionMapper.subscribertoSubscriptionDto(subscriptions);
    }

    @Override
    public SubscriptionDto getBlacklists(long userId) {
        log.debug("Получение списка ID пользователей на которые в черном списке");
        List<BlackList> blackList = blackListRepository.findAllByUserId(userId);
        log.info("GET Запрос на получение списка черного списка пользователя выполнен {}", blackList);
        return subscriptionMapper.blackListSubscriptionDto(blackList);
    }

    @Override
    public List<EventFullResponseDto> getEvents(long userId) {
        return subscriberRepository.findAllByUserId(userId).stream()
                .map(Subscriber::getSubscriber)
                .map(eventFeignClient::getByInitiator)
                .filter(Objects::nonNull)
                .filter(event -> event.getState().equals(EventState.PENDING)
                                 || event.getState().equals(EventState.PUBLISHED))

                .toList();
    }

    private void getUser(long userId, long subscriberId) {
        userFeignClient.validateUser(userId);
        userFeignClient.validateUser(subscriberId);
    }

    private void checkUserBD(long userId, long subscriberId) {
        if (subscriberRepository
                .findByUserIdAndSubscriber(userId, subscriberId)
                .isPresent()) {
            throw new ConditionsNotMetException("Пользователь уже в списке подписчиков на данного человека");
        }
        if (blackListRepository
                .findByUserIdAndBlockUser(userId, subscriberId)
                .isPresent()) {
            throw new ConditionsNotMetException("Пользователь находиться в черном списке и не может подписаться");
        }
    }
}
