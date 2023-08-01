package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    private void isValid(User user) {
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
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUser(int id) {
        return userDao.getUser(id);
    }

    public User createUser(User user) {
        isValid(user);
        return userDao.createUser(user);
    }

    public User updateUser(User user) {
        isValid(user);
        return userDao.updateUser(user);
    }

    public void addFriend(int id, int friendId) {
        userDao.addFriend(id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        userDao.deleteFriend(id, friendId);
    }

    public List<User> getUserFriends(int id) {
        return userDao.getUserFriends(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userDao.getCommonFriends(id, otherId);
    }

}
