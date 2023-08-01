package ru.yandex.practicum.filmorate.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public User checkUser(int id) {
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
        String sql = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        User updatedUser = jdbcTemplate.query(sql, new Object[]{user.getId()}, new UserMapper())
                .stream()
                .findAny()
                .orElse(null);
        if (updatedUser == null) {
            throw new NotFoundException("Такого пользователя в базе нет!");
        }
        String sqlUpdate = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? where user_id = ?";
        jdbcTemplate.update(sqlUpdate, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void addFriend(int id, int friendId) {
        checkUser(id);
        checkUser(friendId);
        String sql = "INSERT INTO users_friends VALUES(?,?)";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        checkUser(id);
        checkUser(friendId);
        String sql = "DELETE FROM users_friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public List<User> getUserFriends(int id) {
        checkUser(id);
        String sql = "SELECT *\n" +
                "FROM USERS u \n" +
                "JOIN USERS_FRIENDS uf ON u.USER_ID  = uf.FRIEND_ID \n" +
                "WHERE uf.USER_ID = ?";
        return jdbcTemplate.query(sql, new UserMapper(), id);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        checkUser(id);
        checkUser(otherId);
        String sql = "SELECT DISTINCT *\n" +
                "FROM USERS u\n" +
                "JOIN USERS_FRIENDS uf ON u.USER_ID = uf.FRIEND_ID\n" +
                "JOIN USERS_FRIENDS uf2 ON u.USER_ID = uf2.FRIEND_ID\n" +
                "WHERE uf.USER_ID = ? AND uf2.USER_ID = ?";
        return jdbcTemplate.query(sql, new UserMapper(), id, otherId);
    }
}
