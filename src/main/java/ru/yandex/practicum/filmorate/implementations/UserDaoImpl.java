package ru.yandex.practicum.filmorate.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    private User checkUser(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        User user = jdbcTemplate.query(sql, new Object[]{id}, new UserMapper())
                .stream()
                .findAny()
                .orElse(null);
        if (user == null) {
            throw new NotFoundException("Такого пользователя в базе нет!");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User getUser(int id) {
        return checkUser(id);
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO users VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql, user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "select * from users where user_id = ?";
        User updatedUser = jdbcTemplate.query(sql, new Object[]{user.getId()}, new UserMapper())
                .stream()
                .findAny()
                .orElse(null);
        if (updatedUser == null) {
            throw new NotFoundException("Такого пользователя в базе нет!");
        }
        String sqlUpdate = "update users set email = ?, login = ?, name = ?, birthday = ? where user_id = ?";
        jdbcTemplate.update(sqlUpdate, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void addFriend(int id, int friendId) {
        checkUser(id);
        checkUser(friendId);
        String sql = "insert into users_friends values(?,?)";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        checkUser(id);
        checkUser(friendId);
        String sql = "delete from users_friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override // здесь нужно будет через join корее всего
    public List<User> getUserFriends(int id) {
        checkUser(id);
        String sql = "";
        return jdbcTemplate.query(sql, new UserMapper(), id);
    }

    @Override //здесь нужен будет подзапрос скорее всего
    public List<User> getCommonFriends(int id, int otherId) {
        checkUser(id);
        checkUser(otherId);
        String sql = "";
        return jdbcTemplate.query(sql, new UserMapper(), id, otherId);
    }
}
