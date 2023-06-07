package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final Map<Integer, Film> films = new HashMap<>();

    private int nextId = 1;


    @GetMapping
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank() || film.getDescription().length() > 200) {
            String exception = "Название фильма не может быть пустым или содержать более 200 символов!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String exception = "Дата фильма не может быть пустой или раньше 28.12.1895!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        if (film.getDuration() <= 0) {
            String exception = "Продолжительность фильма не может быть отрицательной!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        if (films.isEmpty() || film.getId() == 0) {
            film.setId(nextId);
            nextId++;
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank() || film.getDescription().length() > 200) {
            String exception = "Название фильма не может быть пустым или содержать более 200 символов!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String exception = "Дата фильма не может быть пустой или раньше 28.12.1895!";
            log.info(exception);
            throw new ValidationException(exception);
        }

        if (film.getDuration() <= 0) {
            String exception = "Продолжительность фильма не может быть отрицательной!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            String exception = "Такого фильма нет!";
            log.info(exception);
            throw new ValidationException(exception);
        }
        return film;
    }
}
