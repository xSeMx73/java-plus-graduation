package ru.practicum.exception;

import lombok.Getter;

@Getter
public class ParameterNotValidException extends RuntimeException {

    private final String parameter;
    private final String reason;

    public ParameterNotValidException(String parameter, String reason) {
        this.parameter = parameter;
        this.reason = reason;
    }
}
