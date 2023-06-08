package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private static final Map<Integer, User> users = new HashMap<>();

    private int nextId = 1;


    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (isValid(user)) {
            if (users.isEmpty() || user.getId() == 0) {
                user.setId(nextId);
                nextId++;
            }
            users.put(user.getId(), user);
        }
        log.info("Пользователь " + user + " создан успешно");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (isValid(user)) {
            if (users.containsKey(user.getId())) {
                user.setId(user.getId());
                users.put(user.getId(), user);
            } else {
                throw new ValidationException("Такого пользователя нет!");
            }
        }
        log.info("Пользователь" + user + " обновлен успешно");
        return user;
    }

    public boolean isValid(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @!");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть пустой или в будущем!");
        }
        return true;
    }
}
