package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmDao filmDao;

    private final UserDao userDao;

    private void isValid(Film film) {
        if (film.getName() == null || film.getName().isBlank() || film.getDescription().length() > 200) {
            throw new ValidationException("Название фильма не может быть пустым или содержать более 200 символов!");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата фильма не может быть пустой или раньше 28.12.1895!");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной!");
        }
    }


    public List<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    public Film getFilm(int id) {
        return filmDao.getFilm(id);
    }

    public Film createFilm(Film film) {
        isValid(film);
        return filmDao.createFilm(film);
    }

    public Film updateFilm(Film film) {
        isValid(film);
        return filmDao.updateFilm(film);
    }

    public void addLike(int id, int userId) {
        userDao.checkUser(userId);
        filmDao.addLike(id, userId);
    }

    public void removeLike(int id, int userId) {
        userDao.checkUser(userId);
        filmDao.removeLike(id, userId);
    }

    public List<Film> getMostLikedFilms(int count) {
        return filmDao.getMostLikedFilms(count);
    }

}
