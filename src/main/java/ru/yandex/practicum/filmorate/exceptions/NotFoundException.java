package ru.yandex.practicum.filmorate.exceptions;

public class NotFoundException extends NullPointerException {

    public NotFoundException(String message) {
        super(message);
    }
}
