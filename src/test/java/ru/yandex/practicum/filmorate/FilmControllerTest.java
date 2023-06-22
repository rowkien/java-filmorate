package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

class FilmControllerTest {

    FilmService filmService;

    UserStorage userStorage;

    FilmStorage filmStorage;

    @BeforeEach
    public void beforeEach() {
        userStorage = new InMemoryUserStorage();
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage, userStorage);
    }


    @Test
    public void shouldCreateFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("Batman");
        film.setDescription("DC comics movie");
        film.setReleaseDate(LocalDate.of(2008, 7, 18));
        film.setDuration(152);
        Film createdFilm = filmService.createFilm(film);
        Assertions.assertEquals(film, createdFilm);
    }

    @Test
    public void shouldGetAllFilms() {
        Film film = new Film();
        film.setId(1);
        film.setName("Batman");
        film.setDescription("DC comics movie");
        film.setReleaseDate(LocalDate.of(2008, 7, 18));
        film.setDuration(152);
        Film createdFilm = filmService.createFilm(film);
        Assertions.assertEquals(createdFilm, filmService.getAllFilms().get(0));
    }

    @Test
    public void shouldUpdateFilmWithNewDescription() {
        Film film = new Film();
        film.setId(1);
        film.setName("Batman");
        film.setDescription("DC comics movie");
        film.setReleaseDate(LocalDate.of(2008, 7, 18));
        film.setDuration(152);
        filmService.createFilm(film);
        film.setDescription("MOST EPIC ever DC comics movie");
        Film updatedFilm = filmService.updateFilm(film);
        Assertions.assertEquals(updatedFilm.getDescription(), ("MOST EPIC ever DC comics movie"));
    }

    @Test
    public void shouldNotCreateFilmWithEmptyName() {
        Film film = new Film();
        film.setId(1);
        film.setDescription("DC comics movie");
        film.setReleaseDate(LocalDate.of(2008, 7, 18));
        film.setDuration(152);
        try {
            filmService.createFilm(film);
        } catch (ValidationException exception) {
            Assertions.assertEquals(exception.getMessage(), "Название фильма не может быть пустым или содержать более 200 символов!");
        }
    }

    @Test
    public void shouldNotCreateFilmWithBadDescription() {
        Film film = new Film();
        film.setId(1);
        film.setName("Batman");
        film.setDescription("qwerqwerqwerqwerqwerqwerqwerqwerqqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqw" +
                "qwerqwerqwerqwerqwerqwerqwerqwereqwrqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqw" +
                "qwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerq");
        try {
            filmService.createFilm(film);
        } catch (ValidationException exception) {
            Assertions.assertEquals(exception.getMessage(), "Название фильма не может быть пустым или содержать более 200 символов!");
        }
    }

    @Test
    public void shouldNotCreateFilmWithBadReleaseDate() {
        Film film = new Film();
        film.setId(1);
        film.setName("Batman");
        film.setDescription("DC comics movie");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(152);
        try {
            filmService.createFilm(film);
        } catch (ValidationException exception) {
            Assertions.assertEquals(exception.getMessage(), "Дата фильма не может быть пустой или раньше 28.12.1895!");
        }
    }

    @Test
    public void shouldNotCreateFilmWithBadDuration() {
        Film film = new Film();
        film.setId(1);
        film.setName("Batman");
        film.setDescription("DC comics movie");
        film.setReleaseDate(LocalDate.of(2008, 7, 18));
        film.setDuration(-152);
        try {
            filmService.createFilm(film);
        } catch (ValidationException exception) {
            Assertions.assertEquals(exception.getMessage(), "Продолжительность фильма не может быть отрицательной!");
        }
    }

}