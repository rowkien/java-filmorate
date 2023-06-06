package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private static final Map<Integer, User> users = new HashMap<>();

    private int nextId = 1;


    @GetMapping
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String exception = "Электронная почта не может быть пустой и должна содержать символ @!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            String exception = "Логин не может быть пустым и содержать пробелы!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String exception = "Дата рождения не может быть в будущем!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        if (users.isEmpty() || user.getId() == 0) {
            user.setId(nextId);
            nextId++;
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String exception = "Электронная почта не может быть пустой и должна содержать символ @!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            String exception = "Логин не может быть пустым и содержать пробелы!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String exception = "Дата рождения не может быть в будущем!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        if (users.containsKey(user.getId())) {
            user.setId(user.getId());
            users.put(user.getId(), user);
        }
        else {
            String exception = "Такого фильма нет!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        return user;
    }
}
