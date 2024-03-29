package ru.yandex.practicum.filmorate.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    private final GenreDao genreDao;

    public Film checkFilm(int id) {
        String sql = "SELECT * FROM films JOIN mpa ON films.mpa_id = mpa.mpa_id WHERE film_id = ?";
        List<Film> film = jdbcTemplate.query(sql, new FilmMapper(), id);
        if (film.size() == 0) {
            throw new NotFoundException("Фильма с id " + id + "нет в базе!");
        }
        loadFilmGenres(film);
        return film.get(0);
    }

    @Override
    public List<Film> getAllFilms() {
        String filmSql = "SELECT * FROM films JOIN mpa ON films.mpa_id = mpa.mpa_id";
        List<Film> allFilms = jdbcTemplate.query(filmSql, new FilmMapper());
        return loadFilmGenres(allFilms);
    }


    @Override
    public Film getFilm(int id) {
        return checkFilm(id);
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO films (name, description, duration, release_date, mpa_id)" +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        if (film.getGenres() != null) {
            addFilmGenre(film.getGenres(), film.getId());
        }
        return film;
    }


    @Override
    public Film updateFilm(Film film) {
        checkFilm(film.getId());
        String sqlUpdate = "UPDATE films SET name = ?, description = ?, duration = ?, release_date = ?, mpa_id = ? WHERE film_id = ?";
        try {
            jdbcTemplate.update(sqlUpdate, film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate(), film.getMpa().getId(), film.getId());
        } catch (RuntimeException e) {
            throw new NotFoundException("Такого фильма в базе нет!");
        }
        if (film.getGenres() != null) {
            film.setGenres(updateFilmGenre(film.getGenres(), film.getId()));
        }
        return film;
    }

    private List<Genre> updateFilmGenre(List<Genre> filmGenres, int filmId) {
        String sql = "DELETE FROM films_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
        if (filmGenres.size() != 0) {
            filmGenres = addFilmGenre(filmGenres, filmId);
        }
        return filmGenres;
    }

    private List<Genre> addFilmGenre(List<Genre> filmGenres, int filmId) {
        List<Genre> result = new ArrayList<>();
        Set<Integer> genresId = new HashSet<>();
        String sql = "INSERT INTO films_genre (film_id, genre_id) VALUES(?,?)";
        for (Genre genre : filmGenres) {
            genresId.add(genre.getId());
        }
        for (int id : genresId) {
            Genre genre = genreDao.getGenre(id);
            result.add(genre);
            jdbcTemplate.update(sql, filmId, id);
        }
        return result;
    }

    private List<Film> loadFilmGenres(List<Film> films) {
        if (!films.isEmpty()) {
            String genresSql = "SELECT * FROM films_genre JOIN genre ON films_genre.genre_id = genre.genre_id WHERE film_id IN (SELECT film_id FROM films)";
            Map<Integer, List<Genre>> filmGenresMap = new HashMap<>();
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(genresSql);
            for (Map<String, Object> row : rows) {
                int filmId = (int) row.get("film_id");
                Genre genre = new Genre((Integer) row.get("genre_id"), (String) row.get("name"));
                filmGenresMap.computeIfAbsent(filmId, k -> new ArrayList<>()).add(genre);
            }
            for (Film film : films) {
                film.setGenres(filmGenresMap.getOrDefault(film.getId(), new ArrayList<>()));
            }
        }
        return films;
    }

    @Override
    public void addLike(int id, int userId) {
        checkFilm(id);
        String sql = "INSERT INTO films_likes (film_id, user_id) VALUES(?,?)";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public void removeLike(int id, int userId) {
        checkFilm(id);
        String sql = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public List<Film> getMostLikedFilms(int count) {
        List<Film> topFilms;
        String sql = "SELECT * FROM films JOIN mpa ON films.mpa_id = mpa.mpa_id WHERE film_id IN (SELECT film_id FROM films_likes GROUP BY film_id ORDER BY COUNT(*) DESC LIMIT ?)";
        topFilms = jdbcTemplate.query(sql, new Object[]{count}, new FilmMapper());
        if (topFilms.size() == 0) {
            topFilms = getAllFilms();
        }
        return topFilms;
    }
}
