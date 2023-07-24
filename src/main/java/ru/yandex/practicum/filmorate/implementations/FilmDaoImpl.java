package ru.yandex.practicum.filmorate.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    public Film checkFilm(int id) {
        String sql = "select * from films where film_id = ?";
        Film film = jdbcTemplate.query(sql, new Object[]{id}, new FilmMapper())
                .stream()
                .findAny()
                .orElse(null);
        if (film == null) {
            throw new NotFoundException("Такого фильма в базе нет!");
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, new FilmMapper());
    }

    @Override
    public Film getFilm(int id) {
        return checkFilm(id);
    }

    @Override
    public Film createFilm(Film film) { // присваивать жанр и рейтинг
        String sql = "insert into films values(?,?,?,?,?)";
        jdbcTemplate.update(sql, film.getId(), film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {  // присваивать жанр и рейтинг?
        String sql = "select * from films where film_id = ?";
        Film updatedFilm = jdbcTemplate.query(sql, new Object[]{film.getId()}, new FilmMapper())
                .stream()
                .findAny()
                .orElse(null);
        if (updatedFilm == null) {
            throw new NotFoundException("Такого фильма в базе нет!");
        }
        String sqlUpdate = "update films set name = ?, description = ?, duration = ?, release_date = ? where film_id =?";
        jdbcTemplate.update(sqlUpdate, film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate(), film.getId());
        return film;
    }

    @Override
    public void addLike(int id, int userId) {
        checkFilm(id);
        String sql = "insert into films_likes values(?,?)";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public void removeLike(int id, int userId) {
        checkFilm(id);
        String sql = "delete from films_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public List<Film> getMostLikedFilms(int count) {
        String sql = "";
        return null;
    }
}
