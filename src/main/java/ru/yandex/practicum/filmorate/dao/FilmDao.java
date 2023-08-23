package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {
    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilm(int id);

    void addLike(int id, int userId);

    void removeLike(int id, int userId);

    List<Film> getMostLikedFilms(int count);
}
