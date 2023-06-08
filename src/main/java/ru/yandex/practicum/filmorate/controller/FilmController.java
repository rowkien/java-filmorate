package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final Map<Integer, Film> films = new HashMap<>();

    private int nextId = 1;


    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        if (isValid(film)) {
            if (films.isEmpty() || film.getId() == 0) {
                film.setId(nextId);
                nextId++;
            }
            films.put(film.getId(), film);
        }
        log.info("Фильм" + film + " создан успешно");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (isValid(film)) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
            } else {
                throw new ValidationException("Такого фильма нет!");
            }
        }
        log.info("Фильм" + film + " обновлен успешно");
        return film;
    }

    public boolean isValid(Film film) {
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
}
