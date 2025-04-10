package ru.practicum;

import com.google.protobuf.Empty;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.practicum.stats.avro.UserAction.ActionTypeProto;
import ru.practicum.stats.avro.UserAction.UserActionProto;
import ru.practicum.stats.avro.UserActionControllerGrpc;

import java.time.Instant;

@Slf4j
@Component
public class CollectorClient {
    @GrpcClient("collector")
    private UserActionControllerGrpc.UserActionControllerBlockingStub collectorStub;

    public void sendUserAction(long userId, long eventId, ActionTypeProto actionType) {
        try {
            log.info("Отправка действия пользователя: userId={}, eventId={}, actionType={}", userId, eventId, actionType);

            long seconds = Instant.now().getEpochSecond();
            int nanos = Instant.now().getNano();

            UserActionProto request = UserActionProto.newBuilder()
                    .setUserId(userId)
                    .setEventId(eventId)
                    .setActionType(actionType)
                    .setTimestamp(
                            com.google.protobuf.Timestamp.newBuilder()
                                    .setSeconds(seconds)
                                    .setNanos(nanos)
                    )
                    .build();

            Empty response = collectorStub.collectUserAction(request);
            log.info("sendUserAction -> Collector ответил: {}", response);
        } catch (Exception e) {
            log.error("Ошибка при отправке действия пользователя: userId={}, eventId={}, actionType={}",
                    userId, eventId, actionType, e);
        }
    }


    public void sendEventView(long userId, long eventId) {
        sendUserAction(userId, eventId, ActionTypeProto.ACTION_VIEW);
    }

    public void sendEventLike(long userId, long eventId) {
        sendUserAction(userId, eventId, ActionTypeProto.ACTION_LIKE);
    }

    public void sendEventRegistration(long userId, long eventId) {
        sendUserAction(userId, eventId, ActionTypeProto.ACTION_REGISTER);
    }
}