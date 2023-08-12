package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {

    User checkUser(int id);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    List<User> getUserFriends(int id);

    List<User> getCommonFriends(int id, int otherId);

    User getUser(int id);
}
