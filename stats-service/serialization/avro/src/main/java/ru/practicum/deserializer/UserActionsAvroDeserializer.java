package ru.practicum.deserializer;

import ru.practicum.stats.avro.UserActionAvro;

public class UserActionsAvroDeserializer extends BaseAvroDeserializer<UserActionAvro> {
    public UserActionsAvroDeserializer() {
        super(UserActionAvro.getClassSchema());
    }
}