package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    FilmStorage filmStorage;

    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private int nextId = 1;

    private boolean isValid(Film film) {
        if (film.getName() == null || film.getName().isBlank() || film.getDescription().length() > 200) {
            throw new ValidationException("Название фильма не может быть пустым или содержать более 200 символов!");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата фильма не может быть пустой или раньше 28.12.1895!");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной!");
        }
        return true;
    }

    private boolean checkUserId(int id) {
        for (User user : userStorage.getAllUsers()) {
            if (user.getId() == id) {
                return false;
            }
        }
        return true;
    }

    private boolean checkFilmId(int id) {
        for (Film film : filmStorage.getAllFilms()) {
            if (film.getId() == id) {
                return false;
            }
        }
        return true;
    }


    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }


    public Film createFilm(Film film) {
        if (isValid(film)) {
            if (filmStorage.getAllFilms().isEmpty() || film.getId() == 0) {
                film.setId(nextId);
                nextId++;
            }
            filmStorage.createFilm(film);
        }
        log.info("Фильм" + film + " создан успешно");
        return film;
    }


    public Film updateFilm(Film film) {
        if (isValid(film)) {
            for (Film storageFilm : filmStorage.getAllFilms()) {
                if (storageFilm.getId() == film.getId()) {
                    filmStorage.updateFilm(film);
                } else {
                    throw new NotFoundException("Такого фильма нет!");
                }
            }
            log.info("Фильм" + film + " обновлен успешно");
        }
        return film;
    }

    public Film getFilm(int id) {
        if (checkFilmId(id)) {
            throw new NotFoundException("Фильма с ID " + id + " нет в базе!");
        }
        Film film = new Film();
        for (Film element : filmStorage.getAllFilms()) {
            if (element.getId() == id) {
                film = element;
            }
        }
        return film;
    }

    public void addLike(int id, int userId) {
        if (checkFilmId(id)) {
            throw new NotFoundException("Фильма с ID " + id + " нет в базе!");
        }

        if (checkUserId(userId)) {
            throw new NotFoundException("Пользователя с ID " + id + " нет в базе!");
        }
        for (Film film : filmStorage.getAllFilms()) {
            if (film.getId() == id) {
                film.addLike(userId);
            }
        }
    }

    public void removeLike(int id, int userId) {
        if (checkFilmId(id)) {
            throw new NotFoundException("Фильма с ID " + id + " нет в базе!");
        }

        if (checkUserId(userId)) {
            throw new NotFoundException("Пользователя с ID " + id + " нет в базе!");
        }
        for (Film film : filmStorage.getAllFilms()) {
            if (film.getId() == id) {
                film.removeLike(userId);
            }
        }
    }

    public List<Film> getMostLikedFilms(int count) {
        List<Film> mostLikedFilms = new ArrayList<>();
        List<Film> sortedFilms = filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .collect(Collectors.toList());
        if (count == 0) {
            for (int i = 0; i < 10 && i < sortedFilms.size(); i++) {
                mostLikedFilms.add(sortedFilms.get(i));
            }
        } else {
            for (int i = 0; i < count && i < sortedFilms.size(); i++) {
                mostLikedFilms.add(sortedFilms.get(i));
            }
        }
        return mostLikedFilms;
    }

}
