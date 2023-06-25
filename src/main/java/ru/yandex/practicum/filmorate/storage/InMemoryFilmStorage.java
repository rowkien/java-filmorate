package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private static final Map<Integer, Film> films = new HashMap<>();


    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void createFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Фильм" + film + " создан успешно");
    }

    @Override
    public void updateFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Фильм" + film + " обновлен успешно");
    }
}
