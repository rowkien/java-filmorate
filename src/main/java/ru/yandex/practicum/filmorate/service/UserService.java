package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private int nextId = 1;

    private boolean isValid(User user) {
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

    private boolean checkId(int id) {
        for (User user : userStorage.getAllUsers()) {
            if (user.getId() == id) {
                return false;
            }
        }
        return true;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        if (isValid(user)) {
            if (userStorage.getAllUsers().isEmpty() || user.getId() == 0) {
                user.setId(nextId);
                nextId++;
            }
            userStorage.createUser(user);
        }
        log.info("Пользователь " + user + " создан успешно");
        return user;
    }

    public User updateUser(User user) {
        if (isValid(user)) {
            for (User storageUser : userStorage.getAllUsers()) {
                if (storageUser.getId() == user.getId()) {
                    user.setId(user.getId());
                    userStorage.updateUser(user);
                } else {
                    throw new NotFoundException("Такого пользователя нет!");
                }
            }
            log.info("Пользователь " + user + " обновлен успешно");
        }
        return user;
    }

    public void addFriend(int id, int friendId) {
        if (checkId(id)) {
            throw new NotFoundException("Пользователя с ID " + id + " нет в базе!");
        }
        if (checkId(friendId)) {
            throw new NotFoundException("Пользователя с ID " + id + " нет в базе!");
        }
        for (User user : userStorage.getAllUsers()) {
            if (user.getId() == id) {
                user.addFriend(friendId);
            }
        }
        for (User user : userStorage.getAllUsers()) {
            if (user.getId() == friendId) {
                user.addFriend(id);
            }
        }
    }

    public void deleteFriend(int id, int friendId) {
        if (checkId(id)) {
            throw new NotFoundException("Пользователя с ID " + id + " нет в базе!");
        }
        if (checkId(friendId)) {
            throw new NotFoundException("Пользователя с ID " + id + " нет в базе!");
        }
        for (User user : userStorage.getAllUsers()) {
            if (user.getId() == id) {
                user.deleteFriend(friendId);
            }
        }
    }

    public List<User> getUserFriends(int id) {
        if (checkId(id)) {
            throw new NotFoundException("Пользователя с ID " + id + " нет в базе!");
        }
        List<User> userFriends = new ArrayList<>();
        for (User user : userStorage.getAllUsers()) {
            if (user.getId() == id) {
                Set<Integer> friends = user.getFriends();
                for (User element : userStorage.getAllUsers()) {
                    for (int friendId : friends) {
                        if (element.getId() == friendId) {
                            userFriends.add(element);
                        }
                    }
                }
            }
        }
        return userFriends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        if (checkId(id)) {
            throw new NotFoundException("Пользователя с ID " + id + " нет в базе!");
        }
        if (checkId(otherId)) {
            throw new NotFoundException("Пользователя с ID " + id + " нет в базе!");
        }
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> firstUserFriends = new HashSet<>();
        Set<Integer> secondUserFriends = new HashSet<>();
        for (User user : userStorage.getAllUsers()) {
            if (user.getId() == id) {
                firstUserFriends = user.getFriends();
            }
        }
        for (User user : userStorage.getAllUsers()) {
            if (user.getId() == otherId) {
                secondUserFriends = user.getFriends();
            }
        }

        for (User user : userStorage.getAllUsers()) {
            if (firstUserFriends.contains(user.getId()) && secondUserFriends.contains(user.getId())) {
                commonFriends.add(user);
            }
        }
        return commonFriends;
    }

    public User getUser(int id) {
        User user = new User();
        if (checkId(id)) {
            throw new NotFoundException("Пользователя с ID " + id + " нет в базе!");
        }
        for (User element : userStorage.getAllUsers()) {
            if (element.getId() == id) {
                user = element;
            }
        }
        return user;
    }

}
