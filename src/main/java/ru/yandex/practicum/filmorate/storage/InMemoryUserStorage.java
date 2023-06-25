package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private static final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void createUser(User user) {
        users.put(user.getId(), user);
        log.info("Пользователь " + user + " создан успешно");
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
        log.info("Пользователь" + user + " обновлен успешно");
    }
}
