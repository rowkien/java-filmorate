package ru.yandex.practicum.filmorate.exceptions;

public class ValidationException extends NullPointerException {
    public ValidationException(String message) {
        super(message);
    }
}
